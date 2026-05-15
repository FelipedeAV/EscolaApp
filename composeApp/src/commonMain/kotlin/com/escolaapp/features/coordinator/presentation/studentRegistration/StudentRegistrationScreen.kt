package com.escolaapp.features.coordinator.presentation.studentRegistration

import androidx.compose.foundation.background
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
import androidx.compose.material.MaterialTheme
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationForm
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

// ─── Paleta (alinhada ao design system do HTML de referência) ─────────────────

private val PrimaryBlue = Color(0xFF0040A1)
private val SurfaceContainer = Color(0xFFEDEDF8)
private val SurfaceContainerLow = Color(0xFFF2F3FE)
private val SurfaceLowest = Color(0xFFFFFFFF)
private val OnSurfaceVariant = Color(0xFF424654)
private val OutlineVariant = Color(0xFFC3C6D6)
private val SecondaryColor = Color(0xFF8B5000)
private val ErrorColor = Color(0xFFBA1A1A)

// ─── Actions ──────────────────────────────────────────────────────────────────

data class StudentRegistrationActions(
    val onCancel: () -> Unit,
    val onSubmit: () -> Unit,
    val onFullNameChange: (String) -> Unit,
    val onAcademicEmailChange: (String) -> Unit,
    val onGenderChange: (String) -> Unit,
    val onAddressChange: (String) -> Unit,
    val onGuardianNameChange: (String) -> Unit,
    val onGuardianPhoneChange: (String) -> Unit,
    val onGuardianEmailChange: (String) -> Unit,
    val onNotesChange: (String) -> Unit,
)

// ─── Screen ───────────────────────────────────────────────────────────────────

class StudentRegistrationScreen(private val token: String) : Screen {

    @Composable
    override fun Content() {
        val viewModel: StudentRegistrationViewModel = koinInject { parametersOf(token) }
        val navigationViewModel: NavigationViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(viewModel) {
            viewModel.navigationEvents.collect { event ->
                navigationViewModel.emit(event)
            }
        }

        LaunchedEffect(uiState.isSuccess) {
            if (uiState.isSuccess) viewModel.onSuccessDismiss()
        }

        val actions = StudentRegistrationActions(
            onCancel = viewModel::onCancel,
            onSubmit = viewModel::onSubmit,
            onFullNameChange = viewModel::onFullNameChange,
            onAcademicEmailChange = viewModel::onAcademicEmailChange,
            onGenderChange = viewModel::onGenderChange,
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
    val form = uiState.form
    val errors = uiState.fieldErrors

    Scaffold(
        topBar = { RegistrationTopBar(onBack = actions.onCancel) },
        containerColor = Color(0xFFFAF8FF),
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {

            // ── Hero ──────────────────────────────────────────────────────────
            HeroHeader()

            // ── Seção 1: Dados do Aluno ───────────────────────────────────────
            FormSection(
                icon = Icons.Outlined.Person,
                title = "Dados do Aluno",
            ) {
                FormField(
                    label = "Nome Completo",
                    value = form.fullName,
                    onValueChange = actions.onFullNameChange,
                    placeholder = "Ex: Lucas Silva Oliveira",
                    errorMessage = errors["fullName"],
                )
                FormField(
                    label = "E-mail Acadêmico",
                    value = form.academicEmail,
                    onValueChange = actions.onAcademicEmailChange,
                    placeholder = "lucas@editorialacademy.com",
                    keyboardType = KeyboardType.Email,
                    errorMessage = errors["academicEmail"],
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.weight(1f)) {
                        FormField(
                            label = "Data de Nascimento",
                            value = form.birthDate?.toString() ?: "",
                            onValueChange = { /* date picker — ver nota abaixo */ },
                            placeholder = "dd/MM/aaaa",
                            keyboardType = KeyboardType.Number,
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        GenderDropdown(
                            selected = form.gender,
                            onSelect = actions.onGenderChange,
                        )
                    }
                }
                FormField(
                    label = "Endereço Residencial",
                    value = form.address,
                    onValueChange = actions.onAddressChange,
                    placeholder = "Rua, Número, Bairro, Cidade — UF",
                )
            }

            // ── Seção 2: Responsável Legal ────────────────────────────────────
            FormSection(
                icon = Icons.Outlined.FamilyRestroom,
                title = "Responsável Legal"
            ) {
                FormField(
                    label = "Nome do Responsável",
                    value = form.guardianName,
                    onValueChange = actions.onGuardianNameChange,
                    placeholder = "Nome Completo",
                    errorMessage = errors["guardianName"],
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(Modifier.weight(1f)) {
                        FormField(
                            label = "Telefone de Contato",
                            value = form.guardianPhone,
                            onValueChange = actions.onGuardianPhoneChange,
                            placeholder = "(00) 00000-0000",
                            keyboardType = KeyboardType.Phone,
                            errorMessage = errors["guardianPhone"],
                        )
                    }
                    Box(Modifier.weight(1f)) {
                        FormField(
                            label = "E-mail do Responsável",
                            value = form.guardianEmail,
                            onValueChange = actions.onGuardianEmailChange,
                            placeholder = "responsavel@email.com",
                            keyboardType = KeyboardType.Email,
                            errorMessage = errors["guardianEmail"],
                        )
                    }
                }
            }

            // ── Seção 3: Informações Adicionais ───────────────────────────────
            FormSection(
                icon = Icons.Outlined.EditNote,
                title = "Informações Adicionais",
            ) {
                NotesField(
                    value = form.notes,
                    onValueChange = actions.onNotesChange,
                )
            }

            // ── Erro global ───────────────────────────────────────────────────
            if (uiState.errorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFDAD6),
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = ErrorColor,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            uiState.errorMessage,
                            fontSize = 13.sp,
                            color = ErrorColor,
                        )
                    }
                }
            }

            // ── LGPD + Botões ─────────────────────────────────────────────────
            ActionArea(
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
private fun RegistrationTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Outlined.ArrowBackIosNew, contentDescription = "Voltar")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFAF8FF)
        )
    )
}

// ─── Hero header ──────────────────────────────────────────────────────────────

@Composable
private fun HeroHeader() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Novo Registro de Estudante",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlue,
            lineHeight = 32.sp
        )
        Text(
            text = "Preencha os dados abaixo para integrar um novo membro à nossa academia.",
            fontSize = 13.sp,
            color = OnSurfaceVariant,
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
                tint = SecondaryColor,
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
            .background(SurfaceLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    fontSize = 13.sp,
                    color = OnSurfaceVariant.copy(alpha = 0.5f)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = errorMessage != null,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SurfaceContainerLow,
                focusedContainerColor = SurfaceContainerLow,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f),
                errorBorderColor = ErrorColor
            )
        )
        if (errorMessage != null) {
            Text(errorMessage, fontSize = 11.sp, color = ErrorColor)
        }
    }
}

// ─── Gender dropdown ──────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Masculino", "Feminino", "Outro", "Prefiro não informar")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("Gênero", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = OnSurfaceVariant)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selected.ifBlank { "Selecione" },
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f)
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
private fun NotesField(value: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceLowest, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            "Anotações e Observações",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = OnSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            placeholder = {
                Text(
                    "Descreva aqui informações médicas, comportamentais ou pedagógicas relevantes...",
                    fontSize = 12.sp,
                    color = OnSurfaceVariant.copy(alpha = 0.5f),
                    lineHeight = 18.sp
                )
            },
            maxLines = 6,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SurfaceContainerLow,
                focusedContainerColor = SurfaceContainerLow,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = PrimaryBlue.copy(alpha = 0.5f)
            )
        )
    }
}

// ─── Action area ──────────────────────────────────────────────────────────────

@Composable
private fun ActionArea(
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
                tint = OnSurfaceVariant.copy(alpha = 0.7f)
            )
            Text(
                "Os dados serão armazenados conforme a LGPD.",
                fontSize = 11.sp,
                color = OnSurfaceVariant.copy(alpha = 0.7f)
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
                    contentColor = OnSurfaceVariant
                )
            ) {
                Text("Cancelar", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSubmit,
                enabled = !isSubmitting,
                modifier = Modifier.weight(2f),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
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
                    Text("Finalizar Cadastro", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ─── Bottom nav ───────────────────────────────────────────────────────────────

@Composable
private fun RegistrationBottomBar() {
    NavigationBar(containerColor = Color.White.copy(alpha = 0.9f)) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Início") },
            label = { Text("Início", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Outlined.GridView, contentDescription = "Gestão") },
            label = { Text("Gestão", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Outlined.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes", fontSize = 10.sp) }
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
                onGuardianNameChange = {},
                onAddressChange = {},
                onGuardianPhoneChange = {},
                onGuardianEmailChange = {},
                onNotesChange = {},
            ),
        )
    }
}