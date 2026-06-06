package com.escolaapp.features.teacher.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
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
    private val token: String,
    private val teacherId: Int,
) : ScreenModel {

    private val _uiState = MutableStateFlow(TeacherDashboardUiState())
    val uiState: StateFlow<TeacherDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
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
                        error = e.toUserMessage(),
                    )
                }
            }
        }
    }

    fun navigateToClassList(
        mode: ClassListMode,
        name: String,
        email: String,
        role: String,
    ) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToClassList(
                    token = token,
                    teacherId = teacherId,
                    name = name,
                    email = email,
                    role = role,
                    mode = mode,
                )
            )
        }
    }

    fun navigateToSettings(
        name: String,
        email: String,
        role: String,
    ) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToProfile(
                    token = token,
                    userId = teacherId,
                    name = name,
                    email = email,
                    role = role,
                )
            )
        }
    }

    fun onTabSelected(
        tab: AppNavigationTab,
        name: String,
        email: String,
        role: String,
    ) {
        when (tab) {
            AppNavigationTab.CLASSES -> {
                navigateToClassList(
                    mode = ClassListMode.SELECT_ACTION,
                    name = name,
                    email = email,
                    role = role,
                )
            }

            AppNavigationTab.SETTINGS -> {
                navigateToSettings(name, email, role)
            }

            AppNavigationTab.HOME -> Unit
        }
    }

    fun navigateToAddNotice() {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToAddNotice(token = token))
        }
    }

    fun navigateToAttendanceCall() {
        val currentClass = _uiState.value.currentClass ?: return
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToAttendanceCall(
                    token = token,
                    classId = currentClass.id,
                )
            )
        }
    }

    fun navigateToGradeBook(classId: Int, bimester: Int = 1) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToGradeBook(
                    token = token,
                    classId = classId,
                    bimester = bimester,
                )
            )
        }
    }
}