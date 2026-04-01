package com.escolaapp.presentation.attendance

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.AttendanceRepository
import com.escolaapp.domain.model.Attendance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AttendanceUiState(
    val isLoading: Boolean            = false,
    val attendances: List<Attendance> = emptyList(),
    val error: String?                = null
)

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository
) : ScreenModel {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun loadAttendance(token: String, studentId: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val attendances = attendanceRepository.getAttendanceByStudent(token, studentId)
                _uiState.update {
                    it.copy(
                        isLoading   = false,
                        attendances = attendances
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Erro ao carregar frequência"
                    )
                }
            }
        }
    }
}