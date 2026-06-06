package com.escolaapp.features.coordinator.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.coordinator.data.repository.CoordinatorRepository
import com.escolaapp.features.coordinator.domain.model.CoordinatorDashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─── UI State ─────────────────────────────────────────────────────────────────

data class CoordinatorDashboardUiState(
    val isLoading: Boolean = true,
    val dashboard: CoordinatorDashboard? = null,
    val errorMessage: String? = null,
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class CoordinatorDashboardViewModel(
    private val repository: CoordinatorRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
) : ScreenModel {

    private val _uiState = MutableStateFlow(CoordinatorDashboardUiState())
    val uiState: StateFlow<CoordinatorDashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    // ─── Carregamento ─────────────────────────────────────────────────────────

    fun loadDashboard() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.getDashboard(sessionManager.token) }
                .onSuccess { dashboard ->
                    _uiState.update { it.copy(isLoading = false, dashboard = dashboard) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.toUserMessage(),
                        )
                    }
                }
        }
    }

    // ─── Intenções da UI → NavigationEvents ──────────────────────────────────

    fun onQuickActionTap(key: String) {
        val event = when (key) {
            "register_student" -> NavigationEvent.GoToStudentRegistration
            "add_teacher"      -> NavigationEvent.GoToAddTeacher
            else               -> return
        }
        screenModelScope.launch { appEventNavigator.emit(event) }
    }

    fun onManagementCardTap(destination: CoordinatorDestination) {
        val event = when (destination) {
            CoordinatorDestination.CLASSES   -> NavigationEvent.GoToClassManagement
            CoordinatorDestination.SUBJECTS  -> NavigationEvent.GoToSubjectManagement
            CoordinatorDestination.TEACHERS  -> NavigationEvent.GoToTeacherManagement
            CoordinatorDestination.STUDENTS  -> NavigationEvent.GoToStudentManagement
        }
        screenModelScope.launch { appEventNavigator.emit(event) }
    }

    fun onNotificationTap() {
        screenModelScope.launch { appEventNavigator.emit(NavigationEvent.GoToNotifications) }
    }
}

// ─── Enum de destinos internos (presentation-scoped) ─────────────────────────

enum class CoordinatorDestination {
    CLASSES, SUBJECTS, TEACHERS, STUDENTS
}