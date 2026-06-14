package com.escolaapp.features.teacher.presentation.attendance

import com.escolaapp.core.domain.model.StudentAttendanceStatus
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.AttendanceSummaryRepository
import com.escolaapp.features.teacher.domain.model.AttendanceSummary
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AttendanceCallViewModelTest {

    private val testScope = TestScope()
    private val mockRepo = mockk<AttendanceSummaryRepository>(relaxed = true)
    private val mockNavigator = mockk<AppEventNavigator>(relaxed = true)
    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", com.escolaapp.core.domain.model.Role.TEACHER)
    }

    private val sampleSummary = AttendanceSummary(
        classId = 1, subject = "Math", room = "101", period = "morning",
        date = "2025-03-10", totalStudents = 2, presentCount = 0, absentCount = 0, pendingCount = 2,
        students = listOf(
            StudentAttendanceStatus(id = 1, name = "João", status = "pending"),
            StudentAttendanceStatus(id = 2, name = "Maria", status = "absent"),
        ),
    )

    @Test
    fun `init should load summary`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.summary)
        assertEquals("Math", state.summary!!.subject)
        assertEquals(2, state.studentStatuses.size)
    }

    @Test
    fun `init should handle error gracefully`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } throws Exception("Network error")

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `setStudentStatus should update status map`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        vm.setStudentStatus(1, true)

        assertEquals(true, vm.uiState.value.studentStatuses[1])
    }

    @Test
    fun `markAllPresent should set all students as present`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        vm.markAllPresent()

        val state = vm.uiState.value
        assertEquals(true, state.studentStatuses[1])
        assertEquals(true, state.studentStatuses[2])
    }

    @Test
    fun `sendAttendance should call repository`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary
        coEvery { mockRepo.sendBatchAttendance(any(), any(), any(), any(), any()) } returns Unit

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()
        vm.setStudentStatus(1, true)
        vm.setStudentStatus(2, true)

        vm.sendAttendance()
        advanceUntilIdle()

        coVerify { mockRepo.sendBatchAttendance(any(), any(), any(), any(), any()) }
        assertNotNull(vm.uiState.value.success)
    }

    @Test
    fun `setStudentNote should update notes map`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        vm.setStudentNote(1, "Left early")

        assertEquals("Left early", vm.uiState.value.studentNotes[1])
    }

    @Test
    fun `pending status should map to null in initialStatuses`() = runTest {
        coEvery { mockRepo.getSummary(any(), 1, "2025-03-10") } returns sampleSummary

        val vm = AttendanceCallViewModel(
            strings = mockk(relaxed = true),
            repository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            classId = 1,
            initialDate = "2025-03-10",
            coroutineScope = testScope,
        )

        advanceUntilIdle()

        assertNull(vm.uiState.value.studentStatuses[1])
        assertEquals(false, vm.uiState.value.studentStatuses[2])
    }
}
