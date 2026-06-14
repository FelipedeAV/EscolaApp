package com.escolaapp.features.teacher.presentation.addattendance

import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.AttendanceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AddAttendanceViewModelTest {

    private val testScope = TestScope()
    private val mockRepo = mockk<AttendanceRepository>(relaxed = true)
    private val mockNavigator = mockk<AppEventNavigator>(relaxed = true)
    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", com.escolaapp.core.domain.model.Role.TEACHER)
    }

    @Test
    fun `addAttendance should set loading then success`() = runTest {
        coEvery { mockRepo.addAttendance(any(), any()) } returns Unit

        val vm = AddAttendanceViewModel(
            strings = mockk(relaxed = true),
            attendanceRepository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            coroutineScope = testScope,
        )

        vm.addAttendance(studentId = 1, date = "2025-03-10", isPresent = true)
        advanceUntilIdle()

        coVerify { mockRepo.addAttendance("test-token", any()) }
        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.success)
    }

    @Test
    fun `addAttendance should handle error`() = runTest {
        coEvery { mockRepo.addAttendance(any(), any()) } throws Exception("API error")

        val vm = AddAttendanceViewModel(
            strings = mockk(relaxed = true),
            attendanceRepository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            coroutineScope = testScope,
        )

        vm.addAttendance(studentId = 1, date = "2025-03-10", isPresent = true)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }
}
