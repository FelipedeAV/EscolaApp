package com.escolaapp.features.teacher.presentation.gradebook

import com.escolaapp.core.domain.model.ClassGradeSummary
import com.escolaapp.core.domain.model.GradeItem
import com.escolaapp.core.domain.model.Role.TEACHER
import com.escolaapp.core.domain.model.StudentGradeSummary
import com.escolaapp.core.i18n.PtStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.IGradeBookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GradeBookViewModelTest {

    private val savedBatchCalls = mutableListOf<Map<Pair<Int, String>, Double>>()
    private var summaryResult: ClassGradeSummary = ClassGradeSummary(
        classId = 1, subject = "Math", bimester = 1,
        evaluations = listOf("Prova 1"),
        students = emptyList(),
    )
    private var shouldThrow = false

    private val fakeRepo = object : IGradeBookRepository {
        override suspend fun getClassGradeSummary(
            token: String, classId: Int, bimester: Int,
        ): ClassGradeSummary {
            if (shouldThrow) throw Exception("Simulated error")
            return summaryResult
        }

        override suspend fun sendBatchGrades(
            token: String, classId: Int, bimester: Int,
            grades: Map<Pair<Int, String>, Double>,
        ) {
            if (shouldThrow) throw Exception("Simulated error")
            savedBatchCalls.add(grades)
        }
    }

    private val fakeNavigator = object : AppEventNavigator {
        override val events: SharedFlow<NavigationEvent> = MutableSharedFlow()
        override suspend fun emit(event: NavigationEvent) {}
    }

    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", TEACHER)
    }

    private fun createViewModel(scope: TestScope) = GradeBookViewModel(
        strings = PtStrings,
        repository = fakeRepo,
        appEventNavigator = fakeNavigator,
        sessionManager = sessionManager,
        classId = 1,
        coroutineScope = scope,
    )

    @Test
    fun `init should load grades for initial bimester`() = runTest {
        summaryResult = ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1", "Prova 2"),
            students = listOf(
                StudentGradeSummary(
                    id = 1, name = "João",
                    grades = listOf(GradeItem(gradeId = 1, evaluation = "Prova 1", value = 8.0)),
                ),
            ),
        )

        val vm = createViewModel(this)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.summary)
        assertEquals("Math", state.summary.subject)
        assertEquals(1, state.editedGrades.size)
    }

    @Test
    fun `loadGrades should handle error gracefully`() = runTest {
        shouldThrow = true
        val vm = createViewModel(this)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `setGrade should update edited grades and mark hasPendingGrades`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        val state = vm.uiState.value
        assertEquals(9.0, state.editedGrades[Pair(1, "Prova 1")])
        assertEquals(true, state.hasPendingGrades)
        assertTrue(state.unsavedStudents.contains(1))
    }

    @Test
    fun `saveStudentGrades should call repository and clear unsaved`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.saveStudentGrades(1)
        advanceUntilIdle()

        assertEquals(1, savedBatchCalls.size)
        val state = vm.uiState.value
        assertEquals(false, state.isSaving)
        assertEquals(false, state.unsavedStudents.contains(1))
    }

    @Test
    fun `finalizeAllGrades should call repository with all grades`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.finalizeAllGrades()
        advanceUntilIdle()

        assertEquals(1, savedBatchCalls.size)
        val state = vm.uiState.value
        assertEquals(false, state.hasPendingGrades)
    }

    @Test
    fun `cancelChanges should clear unsaved and reload`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.cancelChanges()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.hasPendingGrades)
        assertTrue(state.unsavedStudents.isEmpty())
    }

    @Test
    fun `toggleStudentExpanded should toggle student id`() = runTest {
        val vm = createViewModel(this)

        vm.toggleStudentExpanded(1)
        assertEquals(1, vm.uiState.value.expandedStudentId)

        vm.toggleStudentExpanded(1)
        assertNull(vm.uiState.value.expandedStudentId)
    }
}