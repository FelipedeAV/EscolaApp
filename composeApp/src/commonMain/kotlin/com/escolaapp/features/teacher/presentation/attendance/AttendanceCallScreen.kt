package com.escolaapp.features.teacher.presentation.attendance

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.features.teacher.domain.model.AttendanceSummary
import com.escolaapp.core.domain.model.StudentAttendanceStatus
import com.escolaapp.shared.components.AppActionButton
import com.escolaapp.shared.components.AppErrorState
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.components.AppTopBar
import com.escolaapp.shared.theme.AppColors
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class AttendanceCallScreen(
    val token: String,
    val classId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: AttendanceCallViewModel = koinInject { parametersOf(token, classId) }
        val uiState by viewModel.uiState.collectAsState()

        AttendanceCallScreenContent(
            uiState = uiState,
            onBackClick = { viewModel.navigateBack() },
            onSendAttendance = { viewModel.sendAttendance() },
            onMarkAllPresent = { viewModel.markAllPresent() },
            onSetStudentStatus = { studentId, isPresent ->
                viewModel.setStudentStatus(studentId, isPresent)
            },
        )
    }
}

@Composable
private fun AttendanceCallScreenContent(
    uiState: AttendanceCallUiState,
    onBackClick: () -> Unit,
    onSendAttendance: () -> Unit,
    onMarkAllPresent: () -> Unit,
    onSetStudentStatus: (studentId: Int, isPresent: Boolean) -> Unit,
) {
    if (uiState.isLoading) {
        AppLoadingIndicator()
        return
    }

    uiState.error?.let {
        AppErrorState(message = it)
        return
    }

    val summary = uiState.summary ?: return

    Scaffold(
        topBar = {
            AppTopBar(
                title = "",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
            ) {
                uiState.success?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                AppActionButton(
                    text = if (uiState.isSending) "Enviando..." else "✓ Enviar Frequência",
                    onClick = onSendAttendance,
                    enabled = !uiState.isSending,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = "CHAMADA",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = summary.subject,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Turma A • ${summary.period} • ${summary.room}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "‹",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "HOJE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = uiState.currentDate,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Text(
                        text = "›",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item {
                Text(
                    text = "Total de Alunos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "${summary.totalStudents}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AppColors.SuccessContainer)
                            .padding(16.dp),
                    ) {
                        Column {
                            Text(
                                text = "Presentes",
                                style = MaterialTheme.typography.bodySmall,
                                color = AppColors.Success,
                            )
                            Text(
                                text = "${summary.presentCount}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = AppColors.Success,
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(AppColors.WarningContainer)
                                .padding(16.dp),
                        ) {
                            Column {
                                Text(
                                    text = "Ausentes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.Warning,
                                )
                                Text(
                                    text = "${summary.absentCount}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AppColors.Warning,
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp),
                        ) {
                            Column {
                                Text(
                                    text = "Pendente",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Text(
                                    text = "${summary.pendingCount}",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            OutlinedButton(
                                onClick = onMarkAllPresent,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.38f,
                                    ),
                                ),
                                border = null,
                                content = {
                                    Text("Marcar Todos como Presentes")
                                },
                            )
                        }

                        val sortedStudents = summary.students.sortedBy { it.name.lowercase() }

                        sortedStudents.forEachIndexed { index, student ->
                            val isPresent = uiState.studentStatuses[student.id]

                            AttendanceStudentCard(
                                student = student,
                                isPresent = isPresent,
                                onPresent = { onSetStudentStatus(student.id, true) },
                                onAbsent = { onSetStudentStatus(student.id, false) },
                            )

                            if (index < sortedStudents.lastIndex) {
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
private fun AttendanceStudentCard(
    student: StudentAttendanceStatus,
    isPresent: Boolean?,
    onPresent: () -> Unit,
    onAbsent: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
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
            if (student.consecutiveAbsences >= 3) {
                Text(
                    text = "⚠ ${student.consecutiveAbsences}ª Ausência Consecutiva",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            } else if (student.absenceNote != null) {
                Text(
                    text = student.absenceNote,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                student.lastAttendance?.let {
                    Text(
                        text = "Última presença: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isPresent == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onPresent() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Presente",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPresent == true) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        if (isPresent == false) AppColors.Warning else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onAbsent() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Ausente",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPresent == false) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AttendanceCallScreenContentPreview() {
    MaterialTheme {
        AttendanceCallScreenContent(
            uiState = AttendanceCallUiState(
                summary = previewAttendanceSummary,
                studentStatuses = mapOf(
                    1 to true,
                    2 to false,
                    3 to null,
                ),
                currentDate = "2026-04-12",
            ),
            onBackClick = {},
            onSendAttendance = {},
            onMarkAllPresent = {},
            onSetStudentStatus = { _, _ -> },
        )
    }
}

@Preview
@Composable
private fun AttendanceCallScreenLoadingPreview() {
    MaterialTheme {
        AttendanceCallScreenContent(
            uiState = AttendanceCallUiState(isLoading = true),
            onBackClick = {},
            onSendAttendance = {},
            onMarkAllPresent = {},
            onSetStudentStatus = { _, _ -> },
        )
    }
}

private val previewAttendanceSummary = AttendanceSummary(
    classId = 1,
    subject = "Matemática",
    room = "Sala 08",
    period = "07:30 - 08:20",
    date = "2026-04-12",
    totalStudents = 3,
    presentCount = 1,
    absentCount = 1,
    pendingCount = 1,
    students = listOf(
        StudentAttendanceStatus(
            id = 1,
            name = "Ana Clara",
            status = "present",
            lastAttendance = "2026-04-11",
        ),
        StudentAttendanceStatus(
            id = 2,
            name = "Bruno Souza",
            status = "absent",
            absenceNote = "Atestado médico",
            consecutiveAbsences = 1,
        ),
        StudentAttendanceStatus(
            id = 3,
            name = "Carla Lima",
            status = "pending",
            consecutiveAbsences = 3,
        ),
    ),
)