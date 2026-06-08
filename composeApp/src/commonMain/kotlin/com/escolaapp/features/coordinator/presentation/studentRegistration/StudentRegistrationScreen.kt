package com.escolaapp.features.coordinator.presentation.studentRegistration

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationForm
import com.escolaapp.shared.theme.AppColors
import com.escolaapp.shared.theme.AppColors.OutlineVariant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.koin.compose.koinInject

// ─── Actions ──────────────────────────────────────────────────────────────────

data class StudentRegistrationActions(
    val onCancel: () -> Unit,
    val onSubmit: () -> Unit,
    val onFullNameChange: (String) -> Unit,
    val onAcademicEmailChange: (String) -> Unit,
    val onGenderChange: (String) -> Unit,
    val onBirthDateChange: (String) -> Unit,
    val onAddressChange: (String) -> Unit,
    val onGuardianNameChange: (String) -> Unit,
    val onGuardianPhoneChange: (String) -> Unit,
    val onGuardianEmailChange: (String) -> Unit,
    val onNotesChange: (String) -> Unit,
)

// ─── Screen ───────────────────────────────────────────────────────────────────

class StudentRegistrationScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: StudentRegistrationViewModel = koinInject()
        val sessionManager: SessionManager = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) viewModel.onSuccessDismiss()
        }

        val actions = StudentRegistrationActions(
            onCancel = viewModel::onCancel,
            onSubmit = viewModel::onSubmit,
            onFullNameChange = viewModel::onFullNameChange,
            onAcademicEmailChange = viewModel::onAcademicEmailChange,
            onGenderChange = viewModel::onGenderChange,
            onBirthDateChange = viewModel::onBirthDateChange,
            onAddressChange = viewModel::onAddressChange,
            onGuardianNameChange = viewModel::onGuardianNameChange,
            onGuardianPhoneChange = viewModel::onGuardianPhoneChange,
            onGuardianEmailChange = viewModel::onGuardianEmailChange,
            onNotesChange = viewModel::onNotesChange,
        )

        StudentRegistrationContent(
            uiState = uiState,
            actions = actions,
        )
    }
}

// ─── Stateless content ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StudentRegistrationContent(
    uiState: StudentRegistrationUiState,
    actions: StudentRegistrationActions,
) {
    val s = LocalAppStrings.current
    val form = uiState.form
    val errors = uiState.fieldErrors

    Scaffold(
        topBar = { RegistrationTopBar(s = s, onBack = actions.onCancel) },
        containerColor = AppColors.Background,
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {

            // ── Hero ──────────────────────────────────────────────────────────
            HeroHeader(s = s)

            // ── Seção 1: Dados do Aluno ───────────────────────────────────────
            FormSection(
                icon = Icons.Outlined.Person,
                title = s.coordinator.studentData,
            ) {
                FormField(
                    label = s.coordinator.fullName,
                    value = form.fullName,
                    onValueChange = actions.onFullNameChange,
                    placeholder = s.coordinator.fullNamePlaceholder,
                    errorMessage = errors["fullName"],
                )
                FormField(
                    label = s.coordinator.academicEmail,
                    value = form.academicEmail,
                    onValueChange = actions.onAcademicEmailChange,
                    placeholder = s.coordinator.academicEmailPlaceholder,
                    keyboardType = KeyboardType.Email,
                    errorMessage = errors["academicEmail"],
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.weight(1f)) {
                        BirthDateField(
                            value = form.birthDate,
                            onValueChange = actions.onBirthDateChange,
                            label = s.coordinator.birthDate,
                            placeholder = s.coordinator.birthDatePlaceholder,
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        GenderDropdown(
                            s = s,
                            selected = form.gender,
                            onSelect = actions.onGenderChange,
                        )
                    }
                }
                FormField(
                    label = s.coordinator.address,
                    value = form.address,
                    onValueChange = actions.onAddressChange,
                    placeholder = s.coordinator.addressPlaceholder,
                )
            }

            // ── Seção 2: Responsável Legal ────────────────────────────────────
            FormSection(
                icon = Icons.Outlined.FamilyRestroom,
                title = s.coordinator.legalGuardian
            ) {
                FormField(
                    label = s.coordinator.guardianName,
                    value = form.guardianName,
                    onValueChange = actions.onGuardianNameChange,
                    placeholder = s.coordinator.guardianNamePlaceholder,
                    errorMessage = errors["guardianName"],
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.weight(1f)) {
                        FormField(
                            label = s.coordinator.phone,
                            value = form.guardianPhone,
                            onValueChange = actions.onGuardianPhoneChange,
                            placeholder = s.coordinator.phonePlaceholder,
                            keyboardType = KeyboardType.Phone,
                            errorMessage = errors["guardianPhone"],
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        FormField(
                            label = s.coordinator.guardianEmail,
                            value = form.guardianEmail,
                            onValueChange = actions.onGuardianEmailChange,
                            placeholder = s.coordinator.guardianEmailPlaceholder,
                            keyboardType = KeyboardType.Email,
                            errorMessage = errors["guardianEmail"],
                        )
                    }
                }
            }

            // ── Seção 3: Informações Adicionais ───────────────────────────────
            FormSection(
                icon = Icons.Outlined.EditNote,
                title = s.coordinator.additionalInfo,
            ) {
                NotesField(
                    s = s,
                    value = form.notes,
                    onValueChange = actions.onNotesChange,
                )
            }

            // ── Erro global ───────────────────────────────────────────────────
            if (uiState.errorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AppColors.ErrorContainer,
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = AppColors.Error,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            uiState.errorMessage,
                            fontSize = 13.sp,
                            color = AppColors.Error,
                        )
                    }
                }
            }

            // ── LGPD + Botões ─────────────────────────────────────────────────
            ActionArea(
                s = s,
                isSubmitting = uiState.isSubmitting,
                onCancel = actions.onCancel,
                onSubmit = actions.onSubmit,
            )
        }
    }
}

// ─── Top bar ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegistrationTopBar(s: com.escolaapp.core.i18n.AppStrings, onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = s.common.back)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = AppColors.Background
        )
    )
}

// ─── Hero header ──────────────────────────────────────────────────────────────

@Composable
private fun HeroHeader(s: com.escolaapp.core.i18n.AppStrings) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = s.coordinator.newStudentHeader,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = AppColors.Primary,
            lineHeight = 32.sp
        )
        Text(
            text = s.coordinator.newStudentSubtitle,
            fontSize = 13.sp,
            color = AppColors.OnSurfaceVariant,
            lineHeight = 18.sp
        )
    }
}

// ─── Form section wrapper ─────────────────────────────────────────────────────

@Composable
private fun FormSection(
    icon: ImageVector,
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = AppColors.Secondary,
                modifier = Modifier.size(20.dp)
            )
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        content()
    }
}

// ─── Generic text field ───────────────────────────────────────────────────────

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    errorMessage: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceContainerLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.OnSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    fontSize = 13.sp,
                    color = AppColors.OnSurfaceVariant.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorMessage != null,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppColors.SurfaceContainerLow,
                focusedContainerColor = AppColors.SurfaceContainerLow,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = AppColors.Primary.copy(alpha = 0.5f),
                errorBorderColor = AppColors.Error
            )
        )
        if (errorMessage != null) {
            Text(errorMessage, fontSize = 11.sp, color = AppColors.Error)
        }
    }
}

// ─── Birth date picker ────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthDateField(
    value: LocalDate?,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
) {
    val s = LocalAppStrings.current
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value?.toEpochDays()?.toLong()?.times(86_400_000L)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceContainerLowest, RoundedCornerShape(12.dp))
            .padding(16.dp)
            .clickable { showDatePicker = true },
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AppColors.OnSurfaceVariant)
        OutlinedTextField(
            value = value?.let {
                "${it.dayOfMonth.toString().padStart(2, '0')}/" +
                        "${it.month.number.toString().padStart(2, '0')}/" +
                        "${it.year}"
            } ?: "",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(placeholder, fontSize = 13.sp, color = AppColors.OnSurfaceVariant.copy(alpha = 0.5f))
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppColors.SurfaceContainerLow,
                focusedContainerColor = AppColors.SurfaceContainerLow,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = AppColors.Primary.copy(alpha = 0.5f),
            ),
            enabled = false,
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val epochDay = (millis / 86_400_000L).toInt()
                        val date = LocalDate.fromEpochDays(epochDay)
                        onValueChange(
                            "${date.year}-${date.month.number.toString().padStart(2, '0')}-" +
                                    "${date.dayOfMonth.toString().padStart(2, '0')}"
                        )
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(s.coordinator.cancel)
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// ─── Gender dropdown ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    s: com.escolaapp.core.i18n.AppStrings,
    selected: String,
    onSelect: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf(
        s.coordinator.male,
        s.coordinator.female,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceContainerLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            s.coordinator.gender,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.OnSurfaceVariant,
        )
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selected.ifBlank { s.coordinator.genderSelect },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = AppColors.SurfaceContainerLow,
                    focusedContainerColor = AppColors.SurfaceContainerLow,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = AppColors.Primary.copy(alpha = 0.5f)
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        content = { Text(option, fontSize = 13.sp) },
                        onClick = { onSelect(option); expanded = false }
                    )
                }
            }
        }
    }
}

// ─── Notes textarea ───────────────────────────────────────────────────────────

@Composable
private fun NotesField(
    s: com.escolaapp.core.i18n.AppStrings,
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.SurfaceContainerLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            s.coordinator.notes,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.OnSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            placeholder = {
                Text(
                    s.coordinator.notesPlaceholder,
                    fontSize = 12.sp,
                    color = AppColors.OnSurfaceVariant.copy(alpha = 0.5f),
                    lineHeight = 18.sp
                )
            },
            maxLines = 6,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = AppColors.SurfaceContainerLow,
                focusedContainerColor = AppColors.SurfaceContainerLow,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = AppColors.Primary.copy(alpha = 0.5f)
            )
        )
    }
}

// ─── Action area ──────────────────────────────────────────────────────────────

@Composable
private fun ActionArea(
    s: com.escolaapp.core.i18n.AppStrings,
    isSubmitting: Boolean,
    onCancel: () -> Unit,
    onSubmit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        HorizontalDivider(color = OutlineVariant)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = AppColors.OnSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                s.coordinator.lgpdInfo,
                fontSize = 11.sp,
                color = AppColors.OnSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AppColors.OnSurfaceVariant
                )
            ) {
                Text(s.coordinator.cancel, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSubmit,
                enabled = !isSubmitting,
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary,
                    contentColor = Color.White
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(s.coordinator.finishRegistration, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─── Bottom nav ───────────────────────────────────────────────────────────────

@Composable
private fun RegistrationBottomBar() {
    val s = LocalAppStrings.current
    NavigationBar(containerColor = Color.White.copy(alpha = 0.9f)) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.Home, contentDescription = s.common.home) },
            label = { Text(s.common.home, fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Outlined.GridView, contentDescription = s.common.management) },
            label = { Text(s.common.management, fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.Settings, contentDescription = s.common.settings) },
            label = { Text(s.common.settings, fontSize = 10.sp) }
        )
    }
}

@Preview
@Composable
private fun StudentRegistrationPreview() {
    MaterialTheme {
        StudentRegistrationContent(
            uiState = StudentRegistrationUiState(
                form = StudentRegistrationForm(),
            ),
            actions = StudentRegistrationActions(
                onCancel = {},
                onSubmit = {},
                onFullNameChange = {},
                onAcademicEmailChange = {},
                onGenderChange = {},
                onBirthDateChange = {},
                onGuardianNameChange = {},
                onAddressChange = {},
                onGuardianPhoneChange = {},
                onGuardianEmailChange = {},
                onNotesChange = {},
            ),
        )
    }
}