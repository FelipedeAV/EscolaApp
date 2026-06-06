package com.escolaapp.features.guardian.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.repository.StudentRepository
import com.escolaapp.core.domain.model.Student
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.AppEventNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val student: Student? = null,
    val error: String? = null,
)

class DashboardViewModel(
    private val studentRepository: StudentRepository,
    private val appEventNavigator: AppEventNavigator,
) : ScreenModel {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadStudent(token: String, userId: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val student = studentRepository
                    .getStudents(token)
                    .firstOrNull { it.userId == userId }
                    ?: throw IllegalStateException("Aluno não encontrado para este responsável")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        student = student,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.toUserMessage(),
                    )
                }
            }
        }
    }

    fun navigateToGrades(token: String, studentId: Int) {
        screenModelScope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToGrades(
                    token = token,
                    studentId = studentId,
                )
            )
        }
    }

    fun navigateToAttendance(token: String, studentId: Int) {
        screenModelScope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToAttendance(
                    token = token,
                    studentId = studentId,
                )
            )
        }
    }

    fun navigateToNotices(token: String) {
        screenModelScope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToNotices(token = token)
            )
        }
    }

    fun navigateToAddGrade(token: String) {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToAddGrade(token = token))
        }
    }

    fun navigateToAddAttendance(token: String) {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToAddAttendance(token = token))
        }
    }

    fun navigateToAddNotice(token: String) {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToAddNotice(token = token))
        }
    }

    fun navigateToProfile(token: String, userId: Int, name: String, email: String, role: String) {
        screenModelScope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToProfile(
                    token = token,
                    userId = userId,
                    name = name,
                    email = email,
                    role = role,
                )
            )
        }
    }
}