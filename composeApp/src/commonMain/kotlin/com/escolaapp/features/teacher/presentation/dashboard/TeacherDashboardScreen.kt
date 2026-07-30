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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.shared.components.AppHeader
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.components.AppNavigationBar
import com.escolaapp.shared.components.AppNavigationTab
import com.escolaapp.features.teacher.presentation.components.TeacherActionCard
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.theme.AppColors
import org.koin.compose.koinInject

class TeacherDashboardScreen() : Screen {

    @Composable
    override fun Content() {
        val viewModel: TeacherDashboardViewModel = koinInject()
        val sessionManager: SessionManager = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        var selectedTab by remember { mutableStateOf(AppNavigationTab.HOME) }

        TeacherDashboardContent(
            uiState = uiState,
            name = sessionManager.name,
            selectedTab = selectedTab,
            onTabSelected = { tab -> viewModel.onTabSelected(tab)
            },
            onAttendanceClick = {
                viewModel.navigateToAttendanceCall()
            },
            onGradeBookClick = {
                viewModel.navigateToClassList(mode = ClassListMode.GRADEBOOK)
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
    val s = LocalAppStrings.current

    if (uiState.isLoading) {
        AppLoadingIndicator()
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
                    title = s.common.appName,
                    userInitial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                )
            }

            item {
                Column {
                    Text(
                        text = s.teacher.morningGreeting(name.ifBlank { null }),
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
                    title = s.teacher.makeCallLabel,
                    description = uiState.currentClass?.let {
                        s.teacher.activeSession.replace("{subject}", it.subject).replace("{room}", it.room)
                    } ?: s.teacher.noActiveClass,
                    buttonText = s.teacher.markPresence,
                    onButtonClick = onAttendanceClick,
                    buttonBackgroundColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                    topLabel = s.teacher.quickAccess,
                )
            }

            item {
                TeacherActionCard(
                    icon = Icons.Outlined.AutoStories,
                    iconBackgroundColor = AppColors.SuccessContainer,
                    title = s.teacher.launchGrades,
                    description = uiState.currentClass?.let {
                        s.teacher.updateGradesDesc(it.subject)
                    } ?: s.teacher.selectClassForGrades,
                    buttonText = s.teacher.openGradeBook,
                    onButtonClick = onGradeBookClick,
                    buttonBackgroundColor = MaterialTheme.colorScheme.primary,
                    buttonTextColor = MaterialTheme.colorScheme.onPrimary,
                )
            }

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