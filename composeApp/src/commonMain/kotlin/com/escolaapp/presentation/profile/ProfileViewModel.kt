package com.escolaapp.presentation.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.UserRepository
import com.escolaapp.domain.model.User
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
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
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile(token: String, userId: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getUserById(token, userId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar perfil",
                    )
                }
            }
        }
    }

    fun navigateToSettings(token: String, userId: Int) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToProfileSettings(
                    token = token,
                    userId = userId,
                )
            )
        }
    }

    fun navigateBack() {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.Back)
        }
    }
}



