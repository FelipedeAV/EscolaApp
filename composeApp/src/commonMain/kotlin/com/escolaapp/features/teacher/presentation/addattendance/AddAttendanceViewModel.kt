package com.escolaapp.features.teacher.presentation.addattendance

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.models.AttendanceRequest
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.data.repository.AttendanceRepository
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
    private val strings: AppStrings,
    private val attendanceRepository: AttendanceRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
) : ScreenModel {

    private val _uiState = MutableStateFlow(AddAttendanceUiState())
    val uiState: StateFlow<AddAttendanceUiState> = _uiState.asStateFlow()

    fun addAttendance(studentId: Int, date: String, isPresent: Boolean) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                attendanceRepository.addAttendance(
                    token = sessionManager.token,
                    request = AttendanceRequest(
                        studentId = studentId,
                        date = date,
                        isPresent = isPresent,
                    ),
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = strings.teacher.attendanceSaved,
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
            appEventNavigator.emit(NavigationEvent.GoBack)
        }
    }
}