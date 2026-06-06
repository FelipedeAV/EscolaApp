package com.escolaapp.shared.presentation.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.domain.model.User
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getUserById(sessionManager.token, sessionManager.userId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
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

    fun onTabSelected(tab: AppNavigationTab) {
        when (tab) {
            AppNavigationTab.CLASSES -> navigateToClassList(mode = ClassListMode.SELECT_ACTION)
            AppNavigationTab.SETTINGS -> Unit
            AppNavigationTab.HOME -> navigateToHome()
        }
    }

    fun navigateToClassList(mode: ClassListMode) {
        screenModelScope.launch {
            appEventNavigator.emit(
                NavigationEvent.ToClassList(
                    teacherId = sessionManager.userId,
                    mode = mode,
                )
            )
        }
    }

    fun navigateToHome() {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToDashboard(role = sessionManager.role))
        }
    }

    fun logout() {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.ToLogin)
        }
    }
}