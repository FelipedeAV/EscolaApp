package com.escolaapp.features.teacher.presentation.grade

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.models.GradeRequest
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.session.SessionManager
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.data.repository.IGradeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddGradeUiState(
    val isLoading: Boolean = false,
    val success: String? = null,
    val error: String? = null,
)

class AddGradeViewModel(
    private val strings: AppStrings,
    private val gradeRepository: IGradeRepository,
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val coroutineScope: CoroutineScope? = null,
) : ScreenModel {

    private val scope = coroutineScope ?: screenModelScope
    private val _uiState = MutableStateFlow(AddGradeUiState())
    val uiState: StateFlow<AddGradeUiState> = _uiState.asStateFlow()

    fun addGrade(studentId: Int, subject: String, bimester: Int, value: Double) {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, success = null) }
            try {
                gradeRepository.addGrade(
                    token = sessionManager.token,
                    request = GradeRequest(
                        studentId = studentId,
                        subject = subject,
                        bimester = bimester,
                        value = value,
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = strings.teacher.gradeSaved,
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