package com.escolaapp.presentation.teacher

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.data.repository.AttendanceSummaryRepository
import com.escolaapp.domain.model.AttendanceSummary
import com.escolaapp.navigation.NavigationEvent
import com.escolaapp.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AttendanceCallUiState(
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val summary: AttendanceSummary? = null,
    val studentStatuses: Map<Int, Boolean?> = emptyMap(),
    val studentNotes: Map<Int, String?> = emptyMap(),
    val currentDate: String = "",
    val success: String? = null,
    val error: String? = null,
)

class AttendanceCallViewModel(
    private val repository: AttendanceSummaryRepository,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(AttendanceCallUiState())
    val uiState: StateFlow<AttendanceCallUiState> = _uiState.asStateFlow()

    fun loadSummary(token: String, classId: Int, date: String) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, currentDate = date) }
            try {
                val summary = repository.getSummary(token, classId, date)

                val initialStatuses = summary.students.associate { student ->
                    student.id to when (student.status) {
                        "present" -> true
                        "absent" -> false
                        else -> null
                    }
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        summary = summary,
                        studentStatuses = initialStatuses,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar chamada",
                    )
                }
            }
        }
    }

    fun setStudentStatus(studentId: Int, isPresent: Boolean) {
        _uiState.update { state ->
            state.copy(
                studentStatuses = state.studentStatuses + (studentId to isPresent)
            )
        }
        updateCounts()
    }

    fun setStudentNote(studentId: Int, note: String) {
        _uiState.update { state ->
            state.copy(
                studentNotes = state.studentNotes + (studentId to note)
            )
        }
    }

    fun markAllPresent() {
        _uiState.update { state ->
            val allPresent = state.summary?.students?.associate { it.id to true } ?: emptyMap()
            state.copy(studentStatuses = allPresent)
        }
        updateCounts()
    }

    fun sendAttendance(token: String) {
        val state = _uiState.value
        val summary = state.summary ?: return

        screenModelScope.launch {
            _uiState.update { it.copy(isSending = true, error = null) }
            try {
                repository.sendBatchAttendance(
                    token = token,
                    classId = summary.classId,
                    date = state.currentDate,
                    attendances = state.studentStatuses.filterValues { it != null }
                        .mapValues { it.value!! },
                    notes = state.studentNotes,
                )
                _uiState.update {
                    it.copy(
                        isSending = false,
                        success = "Frequência enviada com sucesso!",
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSending = false,
                        error = "Erro ao enviar frequência",
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

    private fun updateCounts() {
        _uiState.update { state ->
            val summary = state.summary ?: return@update state
            val presentCount = state.studentStatuses.values.count { it == true }
            val absentCount = state.studentStatuses.values.count { it == false }
            val pendingCount = summary.students.size - presentCount - absentCount
            state.copy(
                summary = summary.copy(
                    presentCount = presentCount,
                    absentCount = absentCount,
                    pendingCount = pendingCount,
                )
            )
        }
    }
}