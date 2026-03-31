package com.escolaapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escolaapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean  = false,
    val isSuccess: Boolean  = false,
    val error: String?      = null,
    val token: String?      = null,
    val guardianId: Int?    = null,
    val name: String?       = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val result = authRepository.login(email, password)
                _uiState.update {
                    it.copy(
                        isLoading  = false,
                        isSuccess  = true,
                        token      = result.token,
                        guardianId = result.guardianId,
                        name       = result.name,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Email ou senha inválidos",
                    )
                }
            }
        }
    }
}