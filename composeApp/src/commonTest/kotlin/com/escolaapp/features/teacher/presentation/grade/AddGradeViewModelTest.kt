package com.escolaapp.features.teacher.presentation.grade

import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.GradeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AddGradeViewModelTest {

    private val testScope = TestScope()
    private val mockRepo = mockk<GradeRepository>(relaxed = true)
    private val mockNavigator = mockk<AppEventNavigator>(relaxed = true)
    private val sessionManager = SessionManager().apply {
        save("test-token", 1, "Teacher", "teacher@email.com", com.escolaapp.core.domain.model.Role.TEACHER)
    }

    @Test
    fun `addGrade should set loading then success`() = runTest {
        coEvery { mockRepo.addGrade(any(), any()) } returns Unit

        val vm = AddGradeViewModel(
            strings = mockk(relaxed = true),
            gradeRepository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            coroutineScope = testScope,
        )

        vm.addGrade(studentId = 1, subject = "Math", bimester = 1, value = 8.5)
        advanceUntilIdle()

        coVerify { mockRepo.addGrade("test-token", any()) }
        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.success)
    }

    @Test
    fun `addGrade should handle error`() = runTest {
        coEvery { mockRepo.addGrade(any(), any()) } throws Exception("API error")

        val vm = AddGradeViewModel(
            strings = mockk(relaxed = true),
            gradeRepository = mockRepo,
            appEventNavigator = mockNavigator,
            sessionManager = sessionManager,
            coroutineScope = testScope,
        )

        vm.addGrade(studentId = 1, subject = "Math", bimester = 1, value = 8.5)
        advanceUntilIdle()

        val state = vm.uiState.value
        assertEquals(false, state.isLoading)
        assertNotNull(state.error)
    }
}
