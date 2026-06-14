package com.escolaapp.features.teacher.presentation.notice

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.models.NoticeRequest
import com.escolaapp.core.data.repository.NoticeRepository
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import kotlinx.coroutines.CoroutineScope
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
    private val strings: AppStrings,
    private val noticeRepository: NoticeRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val coroutineScope: CoroutineScope? = null,
) : ScreenModel {

    private val scope = coroutineScope ?: screenModelScope
    private val _uiState = MutableStateFlow(AddNoticeUiState())
    val uiState: StateFlow<AddNoticeUiState> = _uiState.asStateFlow()

    fun addNotice(title: String, description: String) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                noticeRepository.addNotice(
                    token = sessionManager.token,
                    request = NoticeRequest(
                        title = title,
                        description = description,
                    ),
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = strings.teacher.noticePublished,
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

    fun navigateBack() {
        scope.launch {
            appEventNavigator.emit(NavigationEvent.GoBack)
        }
    }
}