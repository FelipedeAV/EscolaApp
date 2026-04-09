package com.escolaapp.presentation.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.presentation.components.TeacherActionCard
import com.escolaapp.presentation.components.teacherAssignedClassesSection
import com.escolaapp.presentation.teacher.ClassListMode
import com.escolaapp.utils.TeacherNavigationBar
import com.escolaapp.utils.TeacherNavigationTab
import org.koin.compose.koinInject

data class TeacherDashboardScreen(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String,
    val role: String,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: TeacherDashboardViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        var selectedTab by remember { mutableStateOf(TeacherNavigationTab.HOME) }

        LaunchedEffect(Unit) {
            viewModel.loadDashboard(token, userId)
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

        Scaffold(
            bottomBar = {
                TeacherNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        viewModel.onTabSelected(tab, token, userId, name, email, role)
                    },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                item {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier         = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text      = "🎓",
                                    fontSize  = 18.sp
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text       = "EscolaApp",
                                style      = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔔", fontSize = 20.sp)
                            Spacer(Modifier.width(12.dp))
                            Box(
                                modifier         = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text      = name.first().uppercaseChar().toString(),
                                    style     = MaterialTheme.typography.titleMedium,
                                    color     = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Hero
                item {
                    Column {
                        Text(
                            text  = "Bom dia, Professor",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                append("Seu ")
                                withStyle(SpanStyle(
                                    fontStyle  = FontStyle.Italic,
                                    color      = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )) {
                                    append("Livro de\nRegistro")
                                }
                                append(" está ativo.")
                            },
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 34.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(
                                    color      = Color(0xFFE67E22),
                                    fontWeight = FontWeight.Bold
                                )) {
                                    append("${uiState.classes.size} aulas")
                                }
                                append(" agendadas\npara hoje.")
                            },
                            fontSize   = 28.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 34.sp
                        )
                    }
                }

                // Card Fazer Chamada
                item {
                    TeacherActionCard(
                        icon = "✋",
                        iconBackgroundColor = Color(0xFFFFF3E0),
                        title = "Fazer Chamada",
                        description = uiState.currentClass?.let {
                            "Sessão Atual: ${it.subject} (${it.room})"
                        } ?: "Nenhuma aula em andamento",
                        buttonText = "Marcar Presença ✓",
                        onButtonClick = {
                            viewModel.navigateToClassList(
                                token = token,
                                teacherId = userId,
                                mode = ClassListMode.ATTENDANCE,
                                name = name,
                                email = email,
                                role = role,
                            )
                        },
                        buttonBackgroundColor = MaterialTheme.colorScheme.surface,
                        buttonTextColor = MaterialTheme.colorScheme.primary,
                        topLabel = "ACESSO RÁPIDO",
                    )
                }

                // Card Lançar Notas
                item {
                    TeacherActionCard(
                        icon = "📖",
                        iconBackgroundColor = Color(0xFFE8F4FD),
                        title = "Lançar Notas",
                        description = uiState.currentClass?.let {
                            "Atualizar notas finais do semestre para ${it.subject}."
                        } ?: "Selecione uma turma para lançar notas.",
                        buttonText = "Abrir Diário de Classe ->",
                        onButtonClick = {
                            viewModel.navigateToClassList(
                                token = token,
                                teacherId = userId,
                                mode = ClassListMode.GRADEBOOK,
                                name = name,
                                email = email,
                                role = role,
                            )
                        },
                        buttonBackgroundColor = MaterialTheme.colorScheme.primary,
                        buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                // Aulas Atribuidas
                teacherAssignedClassesSection(classes = uiState.classes)

                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}