package com.escolaapp.features.guardian.presentation.grades

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.guardian.data.repository.GradeRepository
import com.escolaapp.features.teacher.domain.model.Grade
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GradesUiState(
    val isLoading: Boolean = false,
    val grades: List<Grade> = emptyList(),
    val error: String? = null,
)

class GradesViewModel(
    private val gradeRepository: GradeRepository,
    private val navigationViewModel: NavigationViewModel,
) : ScreenModel {

    private val _uiState = MutableStateFlow(GradesUiState())
    val uiState: StateFlow<GradesUiState> = _uiState.asStateFlow()

    fun loadGrades(token: String, studentId: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val grades = gradeRepository.getGradesByStudent(token, studentId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        grades = grades,
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