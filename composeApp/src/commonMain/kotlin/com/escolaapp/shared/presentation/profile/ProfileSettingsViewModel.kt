package com.escolaapp.shared.presentation.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.core.navigation.AppEventNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileSettingsUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)

class ProfileSettingsViewModel(
    private val userRepository: UserRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val strings: AppStrings,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileSettingsUiState())
    val uiState: StateFlow<ProfileSettingsUiState> = _uiState.asStateFlow()

    fun updatePassword(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
    ) {
        if (newPassword.length < 6) {
            _uiState.update { it.copy(error = strings.profile.passwordMinLength, success = null) }
            return
        }

        if (newPassword != confirmPassword) {
            _uiState.update { it.copy(error = strings.profile.passwordsDontMatch, success = null) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                userRepository.changePassword(sessionManager.token, sessionManager.userId, currentPassword, newPassword)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = strings.profile.passwordChanged,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.toUserMessage(strings),
                    )
                }
            }
        }
    }

    fun navigateBack() {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.GoBack)
        }
    }
}