package com.escolaapp.features.coordinator.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AssignmentInd
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.features.coordinator.domain.model.ActivityType
import com.escolaapp.features.coordinator.domain.model.CoordinatorDashboard
import com.escolaapp.shared.components.AppErrorState
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.features.coordinator.domain.model.QuickAction
import com.escolaapp.features.coordinator.domain.model.RecentActivity
import com.escolaapp.features.coordinator.domain.model.SemesterStats
import com.escolaapp.shared.theme.AppColors
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

data class CoordinatorDashboardScreen(
    val token: String,
    val userId: Int,
    val name: String,
    val email: String,
    val role: String,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: CoordinatorDashboardViewModel = koinInject { parametersOf(token) }
        val uiState by viewModel.uiState.collectAsState()

        CoordinatorDashboardContent(
            uiState = uiState,
            onRetry = viewModel::loadDashboard,
            onQuickAction = viewModel::onQuickActionTap,
            onManagementCard = viewModel::onManagementCardTap,
            onNotification = viewModel::onNotificationTap,
        )
    }
}

@Composable
private fun CoordinatorDashboardContent(
    uiState: CoordinatorDashboardUiState,
    onRetry: () -> Unit,
    onQuickAction: (String) -> Unit,
    onManagementCard: (CoordinatorDestination) -> Unit,
    onNotification: () -> Unit,
) {
    Scaffold(
        topBar = { DashboardTopBar(onNotification) },
        bottomBar = { DashboardBottomBar() },
        containerColor = AppColors.Background,
    ) { padding ->
        when {
            uiState.isLoading -> AppLoadingIndicator(modifier = Modifier.padding(padding))
            uiState.errorMessage != null -> AppErrorState(
                message = uiState.errorMessage,
                onRetry = onRetry,
                modifier = Modifier.padding(padding),
            )
            uiState.dashboard != null -> DashboardBody(
                dashboard = uiState.dashboard,
                onQuickAction = onQuickAction,
                onManagementCard = onManagementCard,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(onNotification: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AppColors.PrimaryVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "EA",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    "The Editorial Academy",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        actions = {
            IconButton(onClick = onNotification) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notificações")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun DashboardBody(
    dashboard: CoordinatorDashboard,
    onQuickAction: (String) -> Unit,
    onManagementCard: (CoordinatorDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {

        // ── Hero ──────────────────────────────────────────────────────────────
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                Text(
                    "Bem-vindo de volta,",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Painel de Coordenação",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
            }
        }

        // ── Quick actions ─────────────────────────────────────────────────────
        item {
            QuickActionsSection(
                actions = dashboard.quickActions,
                pendingCount = dashboard.pendingRequestsCount,
                onAction = onQuickAction
            )
        }

        // ── Management cards grid ─────────────────────────────────────────────
        item {
            Spacer(Modifier.height(16.dp))
            ManagementGrid(onManagementCard)
        }

        // ── Semester overview card ────────────────────────────────────────────
        item {
            Spacer(Modifier.height(8.dp))
            SemesterOverviewCard(
                stats = dashboard.semesterStats,
                activities = dashboard.recentActivities
            )
        }
    }
}

// ─── Quick actions section ────────────────────────────────────────────────────

@Composable
private fun QuickActionsSection(
    actions: List<QuickAction>,
    pendingCount: Int,
    onAction: (String) -> Unit
) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ações Rápidas", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            if (pendingCount > 0) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = AppColors.WarningContainer
                ) {
                    Text(
                        "$pendingCount Novas Solicitações",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        color = AppColors.Secondary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        actions.forEach { action ->
            QuickActionCard(action = action, onClick = { onAction(action.key) })
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun QuickActionCard(action: QuickAction, onClick: () -> Unit) {
    val (iconBg, icon) = when (action.key) {
        "register_student" -> AppColors.SuccessContainer to Icons.Outlined.PersonAdd
        "add_teacher" -> AppColors.WarningContainer to Icons.Outlined.School
        else -> AppColors.AccentGreenContainer to Icons.Default.Add
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(action.label, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                Text(
                    action.subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ─── Management grid ──────────────────────────────────────────────────────────

private data class ManagementItem(
    val destination: CoordinatorDestination,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBg: Color
)

@Composable
private fun ManagementGrid(onTap: (CoordinatorDestination) -> Unit) {
    val items = listOf(
        ManagementItem(
            CoordinatorDestination.CLASSES,
            "Gestão de Turmas",
            "Organize turmas, períodos e horários.",
            Icons.Outlined.Groups,
            AppColors.SuccessContainer
        ),
        ManagementItem(
            CoordinatorDestination.SUBJECTS,
            "Gestão de Disciplinas",
            "Curadoria de conteúdos e grade curricular.",
            Icons.AutoMirrored.Outlined.MenuBook,
            AppColors.WarningContainer
        ),
        ManagementItem(
            CoordinatorDestination.TEACHERS,
            "Gestão de Professores",
            "Acompanhe o corpo docente e atribuições.",
            Icons.Outlined.School,
            AppColors.AccentPurpleContainer
        ),
        ManagementItem(
            CoordinatorDestination.STUDENTS,
            "Gestão de Alunos",
            "Base de dados, desempenho e frequência.",
            Icons.Outlined.AssignmentInd,
            AppColors.AccentGreenContainer
        )
    )

    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.take(2).forEach { item ->
                ManagementCard(
                    item = item,
                    onClick = { onTap(item.destination) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.drop(2).forEach { item ->
                ManagementCard(
                    item = item,
                    onClick = { onTap(item.destination) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ManagementCard(
    item: ManagementItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = androidx.compose.foundation.BorderStroke(
            0.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                item.title,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                item.description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text("Acessar →", fontSize = 11.sp, color = AppColors.PrimaryVariant,)
        }
    }
}

// ─── Semester overview card ───────────────────────────────────────────────────

@Composable
private fun SemesterOverviewCard(
    stats: SemesterStats,
    activities: List<RecentActivity>
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(AppColors.PrimaryVariant)
            .padding(16.dp)
    ) {
        Column {
            Text(
                "Visão Geral do Semestre",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${stats.classesWithCompleteSchedulePercent.toInt()}% das turmas estão com a grade completa " +
                        "e ${stats.newStudentsThisWeek} novos alunos integrados esta semana.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                StatChip(label = "Turmas Ativas", value = stats.activeClasses.toString())
                StatChip(label = "Professores", value = stats.totalTeachers.toString())
            }

            if (activities.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            "ATIVIDADE RECENTE",
                            fontSize = 9.sp,
                            color = Color.White.copy(alpha = 0.75f),
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        activities.take(3).forEach { activity ->
                            ActivityRow(activity)
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String) {
    Column {
        Text(
            label.uppercase(),
            fontSize = 9.sp,
            color = Color.White.copy(alpha = 0.75f),
            letterSpacing = 0.5.sp
        )
        Text(value, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Color.White)
    }
}

@Composable
private fun ActivityRow(activity: RecentActivity) {
    val dotColor = when (activity.type) {
        ActivityType.NEW_SUBJECT -> AppColors.Warning
        ActivityType.ATTENDANCE_DONE -> AppColors.AccentGreen
        ActivityType.NEW_STUDENT -> AppColors.AccentIndigo
        ActivityType.GRADE_BATCH -> AppColors.Success
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        Text(
            activity.description,
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.85f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ─── Bottom nav ───────────────────────────────────────────────────────────────

@Composable
private fun DashboardBottomBar() {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
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

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(name = "Dashboard - Success State")
@Composable
private fun CoordinatorDashboardSuccessPreview() {
    MaterialTheme {
        CoordinatorDashboardContent(
            uiState = CoordinatorDashboardUiState(
                isLoading = false,
                dashboard = previewDashboard,
                errorMessage = null
            ),
            onRetry = {},
            onQuickAction = {},
            onManagementCard = {},
            onNotification = {}
        )
    }
}

@Preview(name = "Dashboard - Loading State")
@Composable
private fun CoordinatorDashboardLoadingPreview() {
    MaterialTheme {
        CoordinatorDashboardContent(
            uiState = CoordinatorDashboardUiState(
                isLoading = true,
                dashboard = null,
                errorMessage = null
            ),
            onRetry = {},
            onQuickAction = {},
            onManagementCard = {},
            onNotification = {}
        )
    }
}

@Preview(name = "Dashboard - Error State")
@Composable
private fun CoordinatorDashboardErrorPreview() {
    MaterialTheme {
        CoordinatorDashboardContent(
            uiState = CoordinatorDashboardUiState(
                isLoading = false,
                dashboard = null,
                errorMessage = "Erro ao carregar o painel. Verifique sua conexão."
            ),
            onRetry = {},
            onQuickAction = {},
            onManagementCard = {},
            onNotification = {}
        )
    }
}

@Preview(name = "Quick Actions Section")
@Composable
private fun QuickActionsSectionPreview() {
    MaterialTheme {
        Surface(color = AppColors.Background) {
            QuickActionsSection(
                actions = previewQuickActions,
                pendingCount = 3,
                onAction = {}
            )
        }
    }
}

@Preview(name = "Management Grid")
@Composable
private fun ManagementGridPreview() {
    MaterialTheme {
        Surface(color = AppColors.Background) {
            ManagementGrid(onTap = {})
        }
    }
}

@Preview(name = "Semester Overview Card")
@Composable
private fun SemesterOverviewCardPreview() {
    MaterialTheme {
        Surface(color = AppColors.Background) {
            SemesterOverviewCard(
                stats = previewSemesterStats,
                activities = previewRecentActivities
            )
        }
    }
}

// ─── Preview Data ─────────────────────────────────────────────────────────────

private val previewQuickActions = listOf(
    QuickAction(
        key = "register_student",
        label = "Cadastrar Novo Aluno",
        subtitle = "Adicione estudantes ao sistema",
        hasPendingAlert = false,
        alertCount = 0
    ),
    QuickAction(
        key = "add_teacher",
        label = "Adicionar Professor",
        subtitle = "Amplie o corpo docente",
        hasPendingAlert = true,
        alertCount = 2
    )
)

private val previewSemesterStats = SemesterStats(
    activeClasses = 12,
    totalTeachers = 24,
    totalStudents = 358,
    globalAverage = 7.5,
    classesWithCompleteSchedulePercent = 85.0,
    newStudentsThisWeek = 5
)

private val previewRecentActivities = listOf(
    RecentActivity(
        type = ActivityType.NEW_SUBJECT,
        description = "Nova disciplina 'Física Avançada' criada",
        relatedEntity = "Física Avançada",
        occurredAt = kotlin.time.Instant.fromEpochMilliseconds(1713024000000L)
    ),
    RecentActivity(
        type = ActivityType.ATTENDANCE_DONE,
        description = "Frequência concluída em 'Matemática A'",
        relatedEntity = "Matemática A",
        occurredAt = kotlin.time.Instant.fromEpochMilliseconds(1713024000000L)
    ),
    RecentActivity(
        type = ActivityType.NEW_STUDENT,
        description = "3 novos alunos integrados hoje",
        relatedEntity = "Alunos",
        occurredAt = kotlin.time.Instant.fromEpochMilliseconds(1713024000000L)
    ),
    RecentActivity(
        type = ActivityType.GRADE_BATCH,
        description = "Notas do 1º bimestre finalizadas",
        relatedEntity = "1º Bimestre",
        occurredAt = kotlin.time.Instant.fromEpochMilliseconds(1713024000000L)
    )
)

private val previewDashboard = CoordinatorDashboard(
    quickActions = previewQuickActions,
    pendingRequestsCount = 3,
    semesterStats = previewSemesterStats,
    recentActivities = previewRecentActivities
)