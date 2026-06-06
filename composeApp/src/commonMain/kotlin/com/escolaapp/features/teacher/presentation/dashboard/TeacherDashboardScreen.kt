package com.escolaapp.features.teacher.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.shared.components.AppHeader
import com.escolaapp.features.teacher.presentation.components.TeacherActionCard
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationBar
import com.escolaapp.shared.components.AppNavigationTab
import com.escolaapp.shared.theme.AppColors
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class TeacherDashboardScreen(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String,
    val role: String,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: TeacherDashboardViewModel = koinInject { parametersOf(token, userId) }
        val uiState by viewModel.uiState.collectAsState()
        var selectedTab by remember { mutableStateOf(AppNavigationTab.HOME) }

        TeacherDashboardContent(
            uiState = uiState,
            name = name,
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                viewModel.onTabSelected(tab, name, email, role)
            },
            onAttendanceClick = {
                viewModel.navigateToAttendanceCall()
            },
            onGradeBookClick = {
                viewModel.navigateToClassList(
                    mode = ClassListMode.GRADEBOOK,
                    name = name,
                    email = email,
                    role = role,
                )
            },
        )
    }
}

@Composable
private fun TeacherDashboardContent(
    uiState: TeacherDashboardUiState,
    name: String,
    selectedTab: AppNavigationTab,
    onTabSelected: (AppNavigationTab) -> Unit,
    onAttendanceClick: () -> Unit,
    onGradeBookClick: () -> Unit,
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        bottomBar = {
            AppNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = onTabSelected,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                AppHeader(
                    icon = Icons.Outlined.School,
                    title = "EscolaApp",
                    userInitial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                )
            }

            item {
                Column {
                    Text(
                        text = if (name.isBlank()) "Bom dia, Professor(a)" else "Bom dia, Professor(a) $name",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            append("Seu ")
                            withStyle(
                                SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append("Livro de\nRegistro")
                            }
                            append(" está ativo.")
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 34.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = AppColors.AccentOrange,
                                    fontWeight = FontWeight.Bold,
                                )
                            ) {
                                append("${uiState.classes.size} aulas")
                            }
                            append(" agendadas\npara hoje.")
                        },
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 34.sp,
                    )
                }
            }

            item {
                Spacer(Modifier.height(16.dp))

                TeacherActionCard(
                    icon = Icons.Outlined.HowToReg,
                    iconBackgroundColor = AppColors.WarningContainer,
                    title = "Fazer Chamada",
                    description = uiState.currentClass?.let {
                        "Sessão Atual: ${it.subject} (${it.room})"
                    } ?: "Nenhuma aula em andamento",
                    buttonText = "Marcar Presença ✓",
                    onButtonClick = onAttendanceClick,
                    buttonBackgroundColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                    topLabel = "ACESSO RÁPIDO",
                )
            }

            item {
                TeacherActionCard(
                    icon = Icons.Outlined.AutoStories,
                    iconBackgroundColor = AppColors.SuccessContainer,
                    title = "Lançar Notas",
                    description = uiState.currentClass?.let {
                        "Atualizar notas finais do semestre para ${it.subject}."
                    } ?: "Selecione uma turma para lançar notas.",
                    buttonText = "Abrir Diário de Classe ->",
                    onButtonClick = onGradeBookClick,
                    buttonBackgroundColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                )
            }

            // TODO: Implementar seção de aulas atribuídas no futuro
            //teacherAssignedClassesSection(classes = uiState.classes)

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Preview
@Composable
private fun TeacherDashboardScreenPreview() {
    MaterialTheme {
        TeacherDashboardContent(
            uiState = TeacherDashboardUiState(
                classes = previewTeacherClasses,
                currentClass = previewTeacherClasses.first(),
            ),
            name = "Carlos Silva",
            selectedTab = AppNavigationTab.HOME,
            onTabSelected = {},
            onAttendanceClick = {},
            onGradeBookClick = {},
        )
    }
}

private val previewTeacherClasses = listOf(
    Class(
        id = 1,
        subject = "Matemática",
        room = "Sala 08",
        schedule = "07:30 - 08:20",
        dayOfWeek = "Quinta-feira",
        teacherId = 1,
    ),
    Class(
        id = 2,
        subject = "Física",
        room = "Laboratório 2",
        schedule = "09:10 - 10:00",
        dayOfWeek = "Quinta-feira",
        teacherId = 1,
    ),
    Class(
        id = 3,
        subject = "Geometria",
        room = "Sala 10",
        schedule = "10:20 - 11:10",
        dayOfWeek = "Quinta-feira",
        teacherId = 1,
    ),
)