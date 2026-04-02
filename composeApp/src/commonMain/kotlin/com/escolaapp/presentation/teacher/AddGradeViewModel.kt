package com.escolaapp.presentation.teacher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.models.GradeRequest
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddGradeUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)

class AddGradeViewModel(
    private val apiClient: ApiClient,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddGradeUiState())
    val uiState: StateFlow<AddGradeUiState> = _uiState.asStateFlow()

    fun addGrade(token: String, studentId: Int, subject: String, bimester: Int, value: Double) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                apiClient.addGrade(
                    token = token,
                    request = GradeRequest(
                        studentId = studentId,
                        subject = subject,
                        bimester = bimester,
                        value = value,
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = "Nota lançada com sucesso!",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao lançar nota",
                    )
                }
            }
        }
    }

    fun navigateBack() {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.Back)
        }
    }
}