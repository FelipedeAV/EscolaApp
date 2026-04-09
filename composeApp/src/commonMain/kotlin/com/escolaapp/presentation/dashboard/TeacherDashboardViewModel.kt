package com.escolaapp.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.ClassRepository
import com.escolaapp.domain.model.Class
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
import com.escolaapp.presentation.teacher.ClassListMode
import com.escolaapp.utils.TeacherNavigationTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeacherDashboardUiState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val currentClass: Class? = null,
    val error: String? = null,
)

class TeacherDashboardViewModel(
    private val classRepository: ClassRepository,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(TeacherDashboardUiState())
    val uiState: StateFlow<TeacherDashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(token: String, teacherId: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val classes = classRepository.getClassesByTeacher(token, teacherId)
                val currentClass = classRepository.getCurrentClass(token, teacherId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        classes = classes,
                        currentClass = currentClass,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar dashboard",
                    )
                }
            }
        }
    }

    fun navigateToClassList(
        token: String,
        teacherId: Int,
        mode: ClassListMode,
        name: String,
        email: String,
        role: String,
    ) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToClassList(
                    token     = token,
                    teacherId = teacherId,
                    name      = name,
                    email     = email,
                    role      = role,
                    mode      = mode
                )
            )
        }
    }

    fun navigateToSettings(token: String, userId: Int, name: String, email: String, role: String) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToProfile(
                    token  = token,
                    userId = userId,
                    name   = name,
                    email  = email,
                    role   = role
                )
            )
        }
    }

    fun onTabSelected(tab: TeacherNavigationTab, token: String, userId: Int, name: String, email: String, role: String) {
        when (tab) {
            TeacherNavigationTab.CLASSES -> {
                navigateToClassList(token, userId, ClassListMode.SELECT_ACTION, name, email, role)
            }

            TeacherNavigationTab.SETTINGS -> {
                navigateToSettings(token, userId, name, email, role)
            }

            TeacherNavigationTab.HOME -> Unit
        }
    }

    fun navigateToAddNotice(token: String) {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToAddNotice(token = token))
        }
    }

    fun navigateToAttendanceCall(token: String, classId: Int) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToAttendanceCall(
                    token = token,
                    classId = classId,
                )
            )
        }
    }

    fun navigateToGradeBook(token: String, classId: Int) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToGradeBook(
                    token = token,
                    classId = classId,
                )
            )
        }
    }
}