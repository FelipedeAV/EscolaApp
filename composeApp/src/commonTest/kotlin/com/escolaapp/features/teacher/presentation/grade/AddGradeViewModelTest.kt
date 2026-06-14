package com.escolaapp.features.teacher.presentation.grade

import com.escolaapp.core.data.models.GradeRequest
import com.escolaapp.core.domain.model.Role.TEACHER
import com.escolaapp.core.i18n.PtStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.IGradeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
@OptIn(ExperimentalCoroutinesApi::class)
class AddGradeViewModelTest {

    private val savedRequests = mutableListOf<GradeRequest>()
    private var shouldThrow = false

    private val fakeRepo = object : IGradeRepository {
        override suspend fun addGrade(token: String, request: GradeRequest) {
            if (shouldThrow) throw Exception("API error")
            savedRequests.add(request)
        }
    }

    private val fakeNavigator = object : AppEventNavigator {
        override val events: SharedFlow<NavigationEvent> = MutableSharedFlow()
        override suspend fun emit(event: NavigationEvent) {}
    }

    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", TEACHER)
    }

    private fun createViewModel(scope: TestScope) = AddGradeViewModel(
        strings = PtStrings,
        gradeRepository = fakeRepo,
        appEventNavigator = fakeNavigator,
        sessionManager = sessionManager,
        coroutineScope = scope,
    )

    @Test
    fun `addGrade should set loading then success`() = runTest {
        val vm = createViewModel(this)
        vm.addGrade(studentId = 1, subject = "Math", bimester = 1, value = 8.5)
        advanceUntilIdle()

        assertEquals(1, savedRequests.size)
        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.success)
    }

    @Test
    fun `addGrade should handle error`() = runTest {
        shouldThrow = true
        val vm = createViewModel(this)
        vm.addGrade(studentId = 1, subject = "Math", bimester = 1, value = 8.5)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }
}