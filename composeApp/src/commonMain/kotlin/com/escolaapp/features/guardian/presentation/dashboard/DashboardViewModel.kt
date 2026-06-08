package com.escolaapp.features.guardian.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.repository.StudentRepository
import com.escolaapp.core.domain.model.Student
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.session.SessionManager
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
    private val sessionManager: SessionManager,
    private val strings: AppStrings,
) : ScreenModel {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadStudent() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val student = if (sessionManager.studentId != 0) {
                    studentRepository.getStudentById(sessionManager.token, sessionManager.studentId)
                } else {
                    val found = studentRepository
                        .getStudents(sessionManager.token)
                        .firstOrNull { it.userId == sessionManager.userId }
                        ?: throw IllegalStateException(strings.guardian.studentNotFound)
                    sessionManager.studentId = found.id
                    found
                }

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

    fun navigateToGrades(studentId: Int) {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToGrades(studentId = studentId))
        }
    }

    fun navigateToAttendance(studentId: Int) {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToAttendance(studentId = studentId))
        }
    }

    fun navigateToNotices() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.ToNotices) }
    }

    fun navigateToAddGrade() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.ToAddGrade) }
    }

    fun navigateToAddAttendance() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.ToAddAttendance) }
    }

    fun navigateToAddNotice() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.ToAddNotice) }
    }

    fun navigateToProfile() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.ToProfile) }
    }
}