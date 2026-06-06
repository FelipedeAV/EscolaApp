package com.escolaapp.features.coordinator.presentation.studentRegistration

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.features.coordinator.data.repository.StudentRegistrationRepository
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationForm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

// ─── UI State ─────────────────────────────────────────────────────────────────

data class StudentRegistrationUiState(
    val form: StudentRegistrationForm = StudentRegistrationForm(),
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val fieldErrors: Map<String, String> = emptyMap(),
)

// ─── ViewModel ────────────────────────────────────────────────────────────────

class StudentRegistrationViewModel(
    private val repository: StudentRegistrationRepository,
    private val token: String,
) : ScreenModel {

    private val _uiState = MutableStateFlow(StudentRegistrationUiState())
    val uiState: StateFlow<StudentRegistrationUiState> = _uiState.asStateFlow()

    val navigationEvents = MutableSharedFlow<NavigationEvent>(extraBufferCapacity = 1)

    // ─── Atualização de campos ────────────────────────────────────────────────

    fun onFullNameChange(value: String) =
        updateForm { copy(fullName = value) }

    fun onBirthDateChange(value: LocalDate?) =
        updateForm { copy(birthDate = value) }

    fun onAcademicEmailChange(value: String) =
        updateForm { copy(academicEmail = value) }

    fun onGenderChange(value: String) =
        updateForm { copy(gender = value) }

    fun onAddressChange(value: String) =
        updateForm { copy(address = value) }

    fun onGuardianNameChange(value: String) =
        updateForm { copy(guardianName = value) }

    fun onGuardianPhoneChange(value: String) =
        updateForm { copy(guardianPhone = value) }

    fun onGuardianEmailChange(value: String) =
        updateForm { copy(guardianEmail = value) }

    fun onNotesChange(value: String) =
        updateForm { copy(notes = value) }

    // ─── Submissão ────────────────────────────────────────────────────────────

    fun onSubmit() {
        val errors = validate(_uiState.value.form)
        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(fieldErrors = errors) }
            return
        }

        screenModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            runCatching { repository.register(token, _uiState.value.form) }
                .onSuccess {
                    _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.message ?: "Erro ao cadastrar aluno",
                        )
                    }
                }
        }
    }

    // ─── Navigations ─────────────────────────────────────────────────────────────
    fun onCancel() = navigationEvents.tryEmit(NavigationEvent.GoBack)

    fun onSuccessDismiss() = navigationEvents.tryEmit(NavigationEvent.GoBack)

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun updateForm(update: StudentRegistrationForm.() -> StudentRegistrationForm) {
        _uiState.update { it.copy(form = it.form.update(), fieldErrors = emptyMap()) }
    }

    private fun validate(form: StudentRegistrationForm): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (form.fullName.isBlank()) errors["fullName"] = "Nome obrigatório"
        if (form.academicEmail.isBlank()) errors["academicEmail"] = "E-mail obrigatório"
        if (form.guardianName.isBlank()) errors["guardianName"] = "Nome do responsável obrigatório"
        if (form.guardianEmail.isBlank()) errors["guardianEmail"] =
            "E-mail do responsável obrigatório"
        if (form.guardianPhone.isBlank()) errors["guardianPhone"] = "Telefone obrigatório"
        return errors
    }
}