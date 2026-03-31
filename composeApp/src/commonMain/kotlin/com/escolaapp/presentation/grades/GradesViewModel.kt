package com.escolaapp.presentation.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escolaapp.data.repository.GradeRepository
import com.escolaapp.domain.model.Grade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GradesUiState(
    val isLoading: Boolean    = false,
    val grades: List<Grade>   = emptyList(),
    val error: String?        = null,
)

class GradesViewModel(
    private val gradeRepository: GradeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GradesUiState())
    val uiState: StateFlow<GradesUiState> = _uiState.asStateFlow()

    fun loadGrades(token: String, studentId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val grades = gradeRepository.getGradesByStudent(token, studentId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        grades    = grades,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error     = "Erro ao carregar notas",
                    )
                }
            }
        }
    }
}