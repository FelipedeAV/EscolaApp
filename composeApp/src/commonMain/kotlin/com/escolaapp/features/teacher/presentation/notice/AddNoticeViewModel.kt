package com.escolaapp.features.teacher.presentation.notice

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.data.models.NoticeRequest
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddNoticeUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)

class AddNoticeViewModel(
    private val apiClient: ApiClient,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddNoticeUiState())
    val uiState: StateFlow<AddNoticeUiState> = _uiState.asStateFlow()

    fun addNotice(token: String, title: String, description: String) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                apiClient.addNotice(
                    token = token,
                    request = NoticeRequest(
                        title = title,
                        description = description,
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = "Aviso publicado com sucesso!",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao publicar aviso",
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
