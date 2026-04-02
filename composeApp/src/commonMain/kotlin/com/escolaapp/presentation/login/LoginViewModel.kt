package com.escolaapp.presentation.login

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.AuthRepository
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = authRepository.login(email, password)
                navigationViewModel.emit(
                    NavigationEvent.ToDashboard(
                        token = result.token,
                        userId = result.userId,
                        name = result.name,
                        role = result.role,
                    )
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Email ou senha inválidos",
                    )
                }
            }
        }
    }
}