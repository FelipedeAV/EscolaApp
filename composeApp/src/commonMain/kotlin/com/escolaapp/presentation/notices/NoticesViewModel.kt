package com.escolaapp.presentation.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escolaapp.data.repository.NoticeRepository
import com.escolaapp.domain.model.Notice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NoticesUiState(
    val isLoading: Boolean        = false,
    val notices: List<Notice>     = emptyList(),
    val error: String?            = null,
)

class NoticesViewModel(
    private val noticeRepository: NoticeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoticesUiState())
    val uiState: StateFlow<NoticesUiState> = _uiState.asStateFlow()

    fun loadNotices(token: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val notices = noticeRepository.getNotices(token)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notices   = notices,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Erro ao carregar avisos",
                    )
                }
            }
        }
    }
}