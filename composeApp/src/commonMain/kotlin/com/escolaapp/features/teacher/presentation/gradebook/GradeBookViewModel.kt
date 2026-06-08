package com.escolaapp.features.teacher.presentation.gradebook

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import com.escolaapp.core.domain.model.ClassGradeSummary
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.AppEventNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GradeBookUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val summary: ClassGradeSummary? = null,
    val editedGrades: Map<Pair<Int, String>, Double> = emptyMap(),
    val unsavedStudents: Set<Int> = emptySet(),
    val expandedStudentId: Int? = null,
    val selectedBimester: Int = 1,
    val success: String? = null,
    val error: String? = null,
    val hasPendingGrades: Boolean = false,
)

class GradeBookViewModel(
    private val strings: AppStrings,
    private val repository: GradeBookRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val classId: Int,
    private val initialBimester: Int = 1,
) : ScreenModel {

    private val _uiState = MutableStateFlow(GradeBookUiState())
    val uiState: StateFlow<GradeBookUiState> = _uiState.asStateFlow()

    init {
        loadGrades(initialBimester)
    }

    fun loadGrades(bimester: Int) {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val summary = repository.getClassGradeSummary(sessionManager.token, classId, bimester)
                val initialGrades = mutableMapOf<Pair<Int, String>, Double>()
                summary.students.forEach { student ->
                    student.grades.forEach { grade ->
                        if (grade.value != null) {
                            initialGrades[Pair(student.id, grade.evaluation)] = grade.value
                        }
                    }
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        summary = summary,
                        editedGrades = initialGrades,
                        selectedBimester = bimester,
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

    fun setGrade(studentId: Int, evaluation: String, value: Double) {
        _uiState.update { state ->
            val key = Pair(studentId, evaluation)
            val newGrades = state.editedGrades + (key to value)
            val unsaved = state.unsavedStudents + studentId
            state.copy(
                editedGrades = newGrades,
                unsavedStudents = unsaved,
                hasPendingGrades = true,
            )
        }
        updateStudentAverage(studentId)
    }

    fun saveStudentGrades(studentId: Int) {
        val state = _uiState.value
        val bimester = state.selectedBimester
        val grades = state.editedGrades.filter { it.key.first == studentId }

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                repository.sendBatchGrades(
                    token = sessionManager.token,
                    classId = classId,
                    bimester = bimester,
                    grades = grades,
                )
                _uiState.update { s ->
                    s.copy(
                        isSaving = false,
                        unsavedStudents = s.unsavedStudents - studentId,
                        hasPendingGrades = s.unsavedStudents.size > 1,
                        success = strings.teacher.gradesSaved,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasPendingGrades = true,
                        error = e.toUserMessage(),
                    )
                }
            }
        }
    }

    fun finalizeAllGrades() {
        val state = _uiState.value
        val grades = state.editedGrades

        screenModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }
            try {
                repository.sendBatchGrades(
                    token = sessionManager.token,
                    classId = classId,
                    bimester = state.selectedBimester,
                    grades = grades,
                )
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        unsavedStudents = emptySet(),
                        hasPendingGrades = false,
                        success = strings.teacher.allGradesFinalized,
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        hasPendingGrades = true,
                        error = e.toUserMessage(),
                    )
                }
            }
        }
    }

    fun cancelChanges() {
        loadGrades(_uiState.value.selectedBimester)
        _uiState.update {
            it.copy(
                unsavedStudents = emptySet(),
                hasPendingGrades = false,
            )
        }
    }

    fun toggleStudentExpanded(studentId: Int) {
        _uiState.update { state ->
            state.copy(
                expandedStudentId = if (state.expandedStudentId == studentId) null else studentId
            )
        }
    }

    fun navigateBack() {
        screenModelScope.launch {
            appEventNavigator.emit(NavigationEvent.GoBack)
        }
    }

    private fun updateStudentAverage(studentId: Int) {
        _uiState.update { state ->
            val summary = state.summary ?: return@update state
            val evaluations = summary.evaluations
            val studentGrades = evaluations.mapNotNull { eval ->
                state.editedGrades[Pair(studentId, eval)]
            }
            val average = if (studentGrades.size == evaluations.size)
                studentGrades.average()
            else null

            val updatedStudents = summary.students.map { student ->
                if (student.id == studentId)
                    student.copy(average = average)
                else student
            }
            state.copy(summary = summary.copy(students = updatedStudents))
        }
    }
}