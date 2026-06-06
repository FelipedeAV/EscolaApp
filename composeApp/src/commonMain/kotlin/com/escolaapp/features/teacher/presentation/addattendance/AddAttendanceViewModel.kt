package com.escolaapp.features.teacher.presentation.addattendance

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.data.models.AttendanceRequest
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.core.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddAttendanceUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)

class AddAttendanceViewModel(
    private val apiClient: ApiClient,
    private val navigationViewModel: NavigationViewModel,
    private val token: String,
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddAttendanceUiState())
    val uiState: StateFlow<AddAttendanceUiState> = _uiState.asStateFlow()

    fun addAttendance(studentId: Int, date: String, isPresent: Boolean) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                apiClient.addAttendance(
                    token = token,
                    request = AttendanceRequest(
                        studentId = studentId,
                        date = date,
                        isPresent = isPresent,
                    ),
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = "Frequência lançada com sucesso!",
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
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.GoBack)
        }
    }
}