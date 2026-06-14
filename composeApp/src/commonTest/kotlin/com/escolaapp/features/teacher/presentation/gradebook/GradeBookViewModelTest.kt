package com.escolaapp.features.teacher.presentation.gradebook

import com.escolaapp.core.domain.model.ClassGradeSummary
import com.escolaapp.core.domain.model.GradeItem
import com.escolaapp.core.domain.model.StudentGradeSummary
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class GradeBookViewModelTest {

    private val testScope = TestScope()
    private val mockRepo = mockk<GradeBookRepository>(relaxed = true)
    private val mockNavigator = mockk<AppEventNavigator>(relaxed = true)
    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", com.escolaapp.core.domain.model.Role.TEACHER)
    }

    private fun createViewModel(classId: Int = 1, bimester: Int = 1): GradeBookViewModel =
        GradeBookViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = classId,
            initialBimester = bimester,
            coroutineScope = testScope,
        )

    @Test
    fun `init should load grades for initial bimester`() = runTest {
        val summary = ClassGradeSummary(
            classId = 1,
            subject = "Math",
            bimester = 1,
            evaluations = listOf("Prova 1", "Prova 2"),
            students = listOf(
                StudentGradeSummary(
                    id = 1, name = "João",
                    grades = listOf(GradeItem(gradeId = 1, evaluation = "Prova 1", value = 8.0)),
                ),
            ),
        )
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns summary

        val vm = createViewModel()

        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.summary)
        assertEquals("Math", state.summary!!.subject)
        assertEquals(1, state.editedGrades.size)
    }

    @Test
    fun `loadGrades should handle error gracefully`() = runTest {
        coEvery { mockRepo.getClassGradeSummary(any(), 1, any()) } throws Exception("Network error")

        val vm = createViewModel()

        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `setGrade should update edited grades and mark hasPendingGrades`() = runTest {
        val summary = ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1"), students = emptyList(),
        )
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns summary

        val vm = createViewModel()
        advanceUntilIdle()

        vm.setGrade(1, "Prova 1", 9.0)

        val state = vm.uiState.value
        assertEquals(9.0, state.editedGrades[Pair(1, "Prova 1")])
        assertEquals(true, state.hasPendingGrades)
        assertTrue(state.unsavedStudents.contains(1))
    }

    @Test
    fun `saveStudentGrades should call repository and clear unsaved`() = runTest {
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1"), students = emptyList(),
        )
        coEvery { mockRepo.sendBatchGrades(any(), any(), any(), any()) } returns Unit

        val vm = createViewModel()
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.saveStudentGrades(1)
        advanceUntilIdle()

        coVerify { mockRepo.sendBatchGrades("test-token", 1, 1, any()) }
        val state = vm.uiState.value
        assertEquals(false, state.isSaving)
        assertEquals(false, state.unsavedStudents.contains(1))
    }

    @Test
    fun `finalizeAllGrades should call repository with all grades`() = runTest {
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1"), students = emptyList(),
        )
        coEvery { mockRepo.sendBatchGrades(any(), any(), any(), any()) } returns Unit

        val vm = createViewModel()
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.finalizeAllGrades()
        advanceUntilIdle()

        coVerify { mockRepo.sendBatchGrades("test-token", 1, 1, any()) }
        val state = vm.uiState.value
        assertEquals(false, state.hasPendingGrades)
    }

    @Test
    fun `cancelChanges should clear unsaved and reload`() = runTest {
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1"), students = emptyList(),
        )

        val vm = createViewModel()
        advanceUntilIdle()
        vm.setGrade(1, "Prova 1", 9.0)

        vm.cancelChanges()
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.hasPendingGrades)
        assertTrue(state.unsavedStudents.isEmpty())
    }

    @Test
    fun `toggleStudentExpanded should toggle student id`() {
        val summary = ClassGradeSummary(
            classId = 1, subject = "Math", bimester = 1,
            evaluations = listOf("Prova 1"),
            students = listOf(StudentGradeSummary(id = 1, name = "João", grades = emptyList())),
        )
        coEvery { mockRepo.getClassGradeSummary(any(), 1, 1) } returns summary

        val vm = createViewModel()
        vm.toggleStudentExpanded(1)
        assertEquals(1, vm.uiState.value.expandedStudentId)

        vm.toggleStudentExpanded(1)
        assertNull(vm.uiState.value.expandedStudentId)
    }
}
