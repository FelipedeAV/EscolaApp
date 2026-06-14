package com.escolaapp.features.teacher.presentation.attendance

import com.escolaapp.core.domain.model.Role.TEACHER
import com.escolaapp.core.domain.model.StudentAttendanceStatus
import com.escolaapp.core.i18n.PtStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.IAttendanceSummaryRepository
import com.escolaapp.features.teacher.domain.model.AttendanceSummary
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

@OptIn(ExperimentalCoroutinesApi::class)
class AttendanceCallViewModelTest {

    private val sentAttendances = mutableListOf<Map<Int, Boolean>>()
    private var summaryResult: AttendanceSummary = AttendanceSummary(
        classId = 1, subject = "Math", room = "101", period = "morning",
        date = "2025-03-10", totalStudents = 2, presentCount = 0, absentCount = 0, pendingCount = 2,
        students = listOf(
            StudentAttendanceStatus(id = 1, name = "João", status = "pending"),
            StudentAttendanceStatus(id = 2, name = "Maria", status = "absent"),
        ),
    )
    private var shouldThrow = false

    private val fakeRepo = object : IAttendanceSummaryRepository {
        override suspend fun getSummary(token: String, classId: Int, date: String): AttendanceSummary {
            if (shouldThrow) throw Exception("Network error")
            return summaryResult
        }

        override suspend fun sendBatchAttendance(
            token: String, classId: Int, date: String,
            attendances: Map<Int, Boolean>, notes: Map<Int, String?>,
        ) {
            if (shouldThrow) throw Exception("Network error")
            sentAttendances.add(attendances)
        }
    }

    private val fakeNavigator = object : AppEventNavigator {
        override val events: SharedFlow<NavigationEvent> = MutableSharedFlow()
        override suspend fun emit(event: NavigationEvent) {}
    }

    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", TEACHER)
    }

    private fun createViewModel(scope: TestScope) = AttendanceCallViewModel(
        strings = PtStrings,
        repository = fakeRepo,
        appEventNavigator = fakeNavigator,
        sessionManager = sessionManager,
        classId = 1,
        initialDate = "2025-03-10",
        coroutineScope = scope,
    )

    @Test
    fun `init should load summary`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.summary)
        assertEquals("Math", state.summary.subject)
        assertEquals(2, state.studentStatuses.size)
    }

    @Test
    fun `init should handle error gracefully`() = runTest {
        shouldThrow = true
        val vm = createViewModel(this)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `setStudentStatus should update status map`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()

        vm.setStudentStatus(1, true)

        assertEquals(true, vm.uiState.value.studentStatuses[1])
    }

    @Test
    fun `markAllPresent should set all students as present`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()

        vm.markAllPresent()

        val state = vm.uiState.value
        assertEquals(true, state.studentStatuses[1])
        assertEquals(true, state.studentStatuses[2])
    }

    @Test
    fun `sendAttendance should call repository`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()
        vm.setStudentStatus(1, true)
        vm.setStudentStatus(2, true)

        vm.sendAttendance()
        advanceUntilIdle()

        assertEquals(1, sentAttendances.size)
        assertNotNull(vm.uiState.value.success)
    }

    @Test
    fun `setStudentNote should update notes map`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()

        vm.setStudentNote(1, "Left early")

        assertEquals("Left early", vm.uiState.value.studentNotes[1])
    }

    @Test
    fun `pending status should map to null in initialStatuses`() = runTest {
        val vm = createViewModel(this)
        advanceUntilIdle()

        assertNull(vm.uiState.value.studentStatuses[1])
        assertEquals(false, vm.uiState.value.studentStatuses[2])
    }
}