package com.escolaapp.features.teacher.presentation.gradebook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.features.teacher.domain.model.ClassGradeSummary
import com.escolaapp.features.teacher.domain.model.GradeItem
import com.escolaapp.core.domain.model.StudentGradeSummary
import com.escolaapp.shared.components.AppActionButton
import com.escolaapp.shared.components.AppHeader
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.theme.AppColors
import com.escolaapp.core.utils.formatOneDecimal
import io.ktor.http.parametersOf
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class GradeBookScreen(
    val token: String,
    val classId: Int,
    val bimester: Int = 1,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: GradeBookViewModel = koinInject { parametersOf(token, classId, bimester) }
        val uiState by viewModel.uiState.collectAsState()

        GradeBookScreenContent(
            uiState = uiState,
            onBackClick = { viewModel.navigateBack() },
            onCancelChanges = { viewModel.cancelChanges() },
            onFinalizeAllGrades = { viewModel.finalizeAllGrades() },
            onToggleStudent = { studentId -> viewModel.toggleStudentExpanded(studentId) },
            onGradeChange = { studentId, evaluation, value ->
                viewModel.setGrade(studentId, evaluation, value)
            },
            onSaveStudent = { studentId ->
                viewModel.saveStudentGrades(studentId)
            },
        )
    }
}

@Composable
private fun GradeBookScreenContent(
    uiState: GradeBookUiState,
    onBackClick: () -> Unit,
    onCancelChanges: () -> Unit,
    onFinalizeAllGrades: () -> Unit,
    onToggleStudent: (Int) -> Unit,
    onGradeChange: (Int, String, Double) -> Unit,
    onSaveStudent: (Int) -> Unit,
) {
    if (uiState.isLoading) {
        AppLoadingIndicator()
        return
    }

    val summary = uiState.summary

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (uiState.unsavedStudents.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(AppColors.WarningContainer)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "i", fontSize = 16.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Voce tem ${uiState.unsavedStudents.size} nota(s) nao salva(s).",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.Warning,
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))


                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                uiState.success?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AppActionButton(
                        text = "Cancelar Alteracoes",
                        onClick = onCancelChanges,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.error,
                    )

                    AppActionButton(
                        text = if (uiState.isSaving) "Salvando..." else "Salvar Notas",
                        onClick = onFinalizeAllGrades,
                        enabled = !uiState.isSaving,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    ) { innerPadding ->
        if (summary == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Não foi possivel carregar as notas.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                AppHeader(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    iconBackgroundColor = MaterialTheme.colorScheme.surface,
                    title = "Notas",
                    userInitial = summary.subject.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                    isTitleCentered = true,
                    onIconClick = onBackClick,
                )
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Badge(
                        text = "${summary.bimester}o Bimestre",
                        color = AppColors.SuccessContainer,
                        textColor = AppColors.Success,
                    )
                    Badge(
                        text = "SESSÃO ATIVA",
                        color = AppColors.WarningContainer,
                        textColor = AppColors.Warning,
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = summary.subject,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(32.dp))
            }

            val sortedStudents = summary.students.sortedBy { it.name.lowercase() }

            itemsIndexed(sortedStudents) { index, student ->
                val isExpanded = uiState.expandedStudentId == student.id
                val hasUnsaved = student.id in uiState.unsavedStudents

                StudentGradeCard(
                    student = student,
                    isExpanded = isExpanded,
                    hasUnsaved = hasUnsaved,
                    editedGrades = uiState.editedGrades,
                    isSaving = uiState.isSaving,
                    onToggle = { onToggleStudent(student.id) },
                    onGradeChange = { evaluation, value ->
                        onGradeChange(student.id, evaluation, value)
                    },
                    onSave = { onSaveStudent(student.id) },
                )

                if (index < sortedStudents.lastIndex) {
                    Spacer(Modifier.height(8.dp))
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun Badge(text: String, color: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun StudentGradeCard(
    student: StudentGradeSummary,
    isExpanded: Boolean,
    hasUnsaved: Boolean,
    editedGrades: Map<Pair<Int, String>, Double>,
    isSaving: Boolean,
    onToggle: () -> Unit,
    onGradeChange: (String, Double) -> Unit,
    onSave: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header do aluno
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = student.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    val average = student.average
                    if (average != null) {
                        Text(
                            text = "Media: ${formatOneDecimal(average)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                average >= 7.0 -> AppColors.Success
                                average >= 5.0 -> AppColors.Warning
                                else -> MaterialTheme.colorScheme.error
                            },
                            fontWeight = FontWeight.SemiBold,
                        )
                    } else {
                        Text(
                            text = "Media: N/D",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppColors.Warning,
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Outlined.KeyboardArrowUp
                    } else {
                        Icons.Outlined.KeyboardArrowDown
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Conteúdo expandido
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    student.grades.forEach { gradeItem ->
                        val currentValue = editedGrades[Pair(student.id, gradeItem.evaluation)]
                        var textValue by remember(currentValue) {
                            mutableStateOf(currentValue?.toString() ?: "")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = gradeItem.evaluation,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                            )

                            OutlinedTextField(
                                value = textValue,
                                onValueChange = { input ->
                                    textValue = input
                                    input.toDoubleOrNull()?.let { value ->
                                        if (value in 0.0..10.0) {
                                            onGradeChange(gradeItem.evaluation, value)
                                        }
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                ),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    textAlign = TextAlign.Center,
                                ),
                                singleLine = true,
                                modifier = Modifier.width(80.dp),
                                shape = RoundedCornerShape(8.dp),
                            )
                        }
                    }

                    val isSaveEnabled = !isSaving && hasUnsaved

                    AppActionButton(
                        text = "Salvar Notas do Aluno",
                        onClick = onSave,
                        enabled = isSaveEnabled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GradeBookScreenContentPreview() {
    MaterialTheme {
        GradeBookScreenContent(
            uiState = GradeBookUiState(
                summary = previewSummary,
                editedGrades = mapOf(
                    Pair(1, "P1") to 8.5,
                    Pair(1, "P2") to 7.0,
                    Pair(2, "P1") to 6.5,
                ),
                unsavedStudents = setOf(1),
                expandedStudentId = 1,
            ),
            onBackClick = {},
            onCancelChanges = {},
            onFinalizeAllGrades = {},
            onToggleStudent = {},
            onGradeChange = { _, _, _ -> },
            onSaveStudent = {},
        )
    }
}

@Preview
@Composable
private fun GradeBookScreenLoadingPreview() {
    MaterialTheme {
        GradeBookScreenContent(
            uiState = GradeBookUiState(isLoading = true),
            onBackClick = {},
            onCancelChanges = {},
            onFinalizeAllGrades = {},
            onToggleStudent = {},
            onGradeChange = { _, _, _ -> },
            onSaveStudent = {},
        )
    }
}

private val previewSummary = ClassGradeSummary(
    classId = 1,
    subject = "Matematica",
    bimester = 1,
    evaluations = listOf("P1", "P2", "Trabalho"),
    students = listOf(
        StudentGradeSummary(
            id = 1,
            name = "Ana Clara",
            average = 7.8,
            grades = listOf(
                GradeItem(evaluation = "P1", value = 8.5),
                GradeItem(evaluation = "P2", value = 7.0),
                GradeItem(evaluation = "Trabalho", value = 8.0),
            ),
        ),
        StudentGradeSummary(
            id = 2,
            name = "Bruno Souza",
            average = null,
            grades = listOf(
                GradeItem(evaluation = "P1", value = 6.5),
                GradeItem(evaluation = "P2", value = null),
                GradeItem(evaluation = "Trabalho", value = null),
            ),
        ),
    ),
)