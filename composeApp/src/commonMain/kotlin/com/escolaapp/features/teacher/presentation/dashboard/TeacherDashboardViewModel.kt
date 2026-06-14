package com.escolaapp.features.teacher.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
import kotlinx.coroutines.CoroutineScope
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
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val coroutineScope: CoroutineScope? = null,
) : ScreenModel {

    private val scope = coroutineScope ?: screenModelScope
    private val _uiState = MutableStateFlow(TeacherDashboardUiState())
    val uiState: StateFlow<TeacherDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val classes = classRepository.getClassesByTeacher(sessionManager.token, sessionManager.userId)
                val currentClass = classRepository.getCurrentClass(sessionManager.token, sessionManager.userId)
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

    fun navigateToClassList(mode: ClassListMode) {
        scope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToClassList(
                    teacherId = sessionManager.userId,
                    mode = mode,
                )
            )
        }
    }

    fun navigateToSettings() {
        scope.launch {
            appEventNavigator.emit(NavigationEvent.ToProfile)
        }
    }

    fun onTabSelected(tab: AppNavigationTab) {
        when (tab) {
            AppNavigationTab.CLASSES -> navigateToClassList(mode = ClassListMode.SELECT_ACTION)
            AppNavigationTab.SETTINGS -> navigateToSettings()
            AppNavigationTab.HOME -> Unit
        }
    }

    fun navigateToAddNotice() {
        scope.launch { appEventNavigator.emit(NavigationEvent.ToAddNotice) }
    }

    fun navigateToAttendanceCall() {
        val currentClass = _uiState.value.currentClass ?: return
        scope.launch {
            appEventNavigator.emit(NavigationEvent.ToAttendanceCall(classId = currentClass.id))
        }
    }

    fun navigateToGradeBook(classId: Int, bimester: Int = 1) {
        scope.launch {
            appEventNavigator.emit(NavigationEvent.ToGradeBook(classId = classId, bimester = bimester))
        }
    }
}