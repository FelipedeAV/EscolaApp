package com.escolaapp.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.StudentRepository
import com.escolaapp.domain.model.Student
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = false,
    val student: Student?  = null,
    val error: String?     = null
)

class DashboardViewModel(
    private val studentRepository: StudentRepository,
    private val navigationViewModel: NavigationViewModel
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
                        student   = student
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Erro ao carregar dados do aluno"
                    )
                }
            }
        }
    }

    fun navigateToGrades(token: String, studentId: Int) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToGrades(
                    token     = token,
                    studentId = studentId
                )
            )
        }
    }

    fun navigateToAttendance(token: String, studentId: Int) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToAttendance(
                    token     = token,
                    studentId = studentId
                )
            )
        }
    }

    fun navigateToNotices(token: String) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToNotices(token = token)
            )
        }
    }

    fun navigateToAddGrade(token: String) {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToAddGrade(token = token))
        }
    }

    fun navigateToAddAttendance(token: String) {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToAddAttendance(token = token))
        }
    }

    fun navigateToAddNotice(token: String) {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToAddNotice(token = token))
        }
    }

    fun navigateBack() {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.Back)
        }
    }
}