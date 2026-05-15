package com.escolaapp.features.coordinator.presentation.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.features.coordinator.data.repository.CoordinatorRepository
import com.escolaapp.features.coordinator.domain.model.CoordinatorDashboard
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val token: String,
) : ScreenModel {

    private val _uiState = MutableStateFlow(CoordinatorDashboardUiState())
    val uiState: StateFlow<CoordinatorDashboardUiState> = _uiState.asStateFlow()

    val navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)

    init {
        loadDashboard()
    }

    // ─── Carregamento ─────────────────────────────────────────────────────────

    fun loadDashboard() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.getDashboard(token) }
                .onSuccess { dashboard ->
                    _uiState.update { it.copy(isLoading = false, dashboard = dashboard) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Erro ao carregar o painel",
                        )
                    }
                }
        }
    }

    // ─── Intenções da UI → NavigationEvents ──────────────────────────────────

    fun onQuickActionTap(key: String) {
        val event = when (key) {
            "register_student" -> NavigationEvent.GoToStudentRegistration(token = token)
            "add_teacher"      -> NavigationEvent.GoToAddTeacher
            else               -> return
        }
        navigationEvents.tryEmit(event)
    }

    fun onManagementCardTap(destination: CoordinatorDestination) {
        val event = when (destination) {
            CoordinatorDestination.CLASSES   -> NavigationEvent.GoToClassManagement
            CoordinatorDestination.SUBJECTS  -> NavigationEvent.GoToSubjectManagement
            CoordinatorDestination.TEACHERS  -> NavigationEvent.GoToTeacherManagement
            CoordinatorDestination.STUDENTS  -> NavigationEvent.GoToStudentManagement
        }
        navigationEvents.tryEmit(event)
    }

    fun onNotificationTap() = navigationEvents.tryEmit(NavigationEvent.GoToNotifications)
//    fun onSettingsTap()     = navigationEvents.tryEmit(NavigationEvent.GoToSettings)
//    fun onProfileTap()      = navigationEvents.tryEmit(NavigationEvent.GoToProfile)
}

// ─── Enum de destinos internos (presentation-scoped) ─────────────────────────

enum class CoordinatorDestination {
    CLASSES, SUBJECTS, TEACHERS, STUDENTS
}