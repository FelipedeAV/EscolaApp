package com.escolaapp.features.teacher.presentation.addattendance

import com.escolaapp.core.data.models.AttendanceRequest
import com.escolaapp.core.i18n.PtStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.IAttendanceRepository
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
class AddAttendanceViewModelTest {

    private val savedRequests = mutableListOf<AttendanceRequest>()
    private var shouldThrow = false

    private val fakeRepo = object : IAttendanceRepository {
        override suspend fun addAttendance(token: String, request: AttendanceRequest) {
            if (shouldThrow) throw Exception("API error")
            savedRequests.add(request)
        }
    }

    private val fakeNavigator = object : AppEventNavigator {
        override val events: SharedFlow<com.escolaapp.core.navigation.NavigationEvent> = MutableSharedFlow()
        override suspend fun emit(event: com.escolaapp.core.navigation.NavigationEvent) {}
    }

    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", com.escolaapp.core.domain.model.Role.TEACHER)
    }

    private fun createViewModel(scope: TestScope) = AddAttendanceViewModel(
        strings = PtStrings,
        attendanceRepository = fakeRepo,
        appEventNavigator = fakeNavigator,
        sessionManager = sessionManager,
        coroutineScope = scope,
    )

    @Test
    fun `addAttendance should set loading then success`() = runTest {
        val vm = createViewModel(this)
        vm.addAttendance(studentId = 1, date = "2025-03-10", isPresent = true)
        advanceUntilIdle()

        assertEquals(1, savedRequests.size)
        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.success)
    }

    @Test
    fun `addAttendance should handle error`() = runTest {
        shouldThrow = true
        val vm = createViewModel(this)
        vm.addAttendance(studentId = 1, date = "2025-03-10", isPresent = true)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }
}