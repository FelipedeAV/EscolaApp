package com.escolaapp.presentation.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escolaapp.data.repository.AttendanceRepository
import com.escolaapp.domain.model.Attendance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AttendanceUiState(
    val isLoading: Boolean          = false,
    val attendances: List<Attendance> = emptyList(),
    val error: String?              = null,
)

class AttendanceViewModel(
    private val attendanceRepository: AttendanceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceUiState())
    val uiState: StateFlow<AttendanceUiState> = _uiState.asStateFlow()

    fun loadAttendance(token: String, studentId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val attendances = attendanceRepository.getAttendanceByStudent(token, studentId)
                _uiState.update {
                    it.copy(
                        isLoading   = false,
                        attendances = attendances,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Erro ao carregar frequência",
                    )
                }
            }
        }
    }
}