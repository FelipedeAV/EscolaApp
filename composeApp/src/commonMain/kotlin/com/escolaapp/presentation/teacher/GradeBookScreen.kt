package com.escolaapp.presentation.teacher

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.domain.model.StudentGradeSummary
import com.escolaapp.utils.formatOneDecimal
import org.koin.compose.koinInject

data class GradeBookScreen(
    val token: String,
    val classId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: GradeBookViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadGrades(token, classId, 1)
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
            return
        }

        val summary = uiState.summary ?: return

        Scaffold(
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    // Aviso de notas não salvas
                    if (uiState.unsavedStudents.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF3E0))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = "ℹ", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Você tem ${uiState.unsavedStudents.size} nota(s) não salva(s).",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE65100),
                            )
                        }
                    }

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.cancelChanges(token, classId) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50.dp),
                        ) {
                            Text(
                                text = "Cancelar Alterações",
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        Button(
                            onClick = { viewModel.finalizeAllGrades(token, classId) },
                            enabled = !uiState.isSaving,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            } else {
                                Text("💾 Finalizar Notas")
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Header
                item {
                    Spacer(Modifier.height(8.dp))

                    // Badges
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Badge(
                            text = "1º Bimestre (B1)",
                            color = Color(0xFFE8F4FD),
                            textColor = Color(0xFF1565C0),
                        )
                        Badge(
                            text = "SESSÃO ATIVA",
                            color = Color(0xFFFFF3E0),
                            textColor = Color(0xFFE65100),
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = summary.subject,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.height(4.dp))
                }

                // Lista de alunos
                items(summary.students) { student ->
                    val isExpanded = uiState.expandedStudentId == student.id
                    val hasUnsaved = student.id in uiState.unsavedStudents

                    StudentGradeCard(
                        student = student,
                        isExpanded = isExpanded,
                        hasUnsaved = hasUnsaved,
                        editedGrades = uiState.editedGrades,
                        isSaving = uiState.isSaving,
                        onToggle = { viewModel.toggleStudentExpanded(student.id) },
                        onGradeChange = { evaluation, value ->
                            viewModel.setGrade(student.id, evaluation, value)
                        },
                        onSave = {
                            viewModel.saveStudentGrades(token, classId, student.id)
                        },
                    )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
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
                        text = student.name.first().uppercaseChar().toString(),
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
                            text = "Média: ${formatOneDecimal(average)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                average >= 7.0 -> Color(0xFF1565C0)
                                average >= 5.0 -> Color(0xFFE65100)
                                else -> MaterialTheme.colorScheme.error
                            },
                            fontWeight = FontWeight.SemiBold,
                        )
                    } else {
                        Text(
                            text = "Média: N/D",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE65100),
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }

                Text(
                    text = if (isExpanded) "∧" else "∨",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                singleLine = true,
                                modifier = Modifier.width(80.dp),
                                shape = RoundedCornerShape(8.dp),
                            )
                        }
                    }

                    Button(
                        onClick = onSave,
                        enabled = !isSaving && hasUnsaved,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                    ) {
                        Text("💾 Salvar Notas do Aluno")
                    }
                }
            }
        }
    }

}