package com.escolaapp.features.guardian.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.session.SessionManager
import com.escolaapp.shared.components.AppErrorState
import com.escolaapp.core.domain.model.Role
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.components.AppTopBar
import org.koin.compose.koinInject

class DashboardScreen() : Screen {

    @Composable
    override fun Content() {
        val viewModel: DashboardViewModel = koinInject()
        val sessionManager: SessionManager = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        val isTeacher = sessionManager.role == Role.TEACHER
        val linkedStudentId = uiState.student?.id

        LaunchedEffect(isTeacher) {
            if (!isTeacher) {
                viewModel.loadStudent()
            }
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Dashboard",
                    showBackButton = false,
                    actions = {
                        IconButton(onClick = { viewModel.navigateToProfile() }) {
                            Text(text = "P")
                        }
                    },
                )
            },
        ) { innerPadding ->
            if (uiState.isLoading) {
                AppLoadingIndicator(modifier = Modifier.padding(innerPadding))
                return@Scaffold
            }

            uiState.error?.let {
                AppErrorState(message = it, modifier = Modifier.padding(innerPadding))
                return@Scaffold
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Olá, ${sessionManager.name}",
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = if (isTeacher) "Professor" else "Responsável",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                uiState.student?.let { student ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Aluno",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                text = student.name,
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Text(
                                text = student.classroom,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Botões visíveis para todos
                Button(
                    onClick = { linkedStudentId?.let { viewModel.navigateToGrades(it) } },
                    enabled = linkedStudentId != null,
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Ver Notas") }

                Button(
                    onClick = { linkedStudentId?.let { viewModel.navigateToAttendance(it) } },
                    enabled = linkedStudentId != null,
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Ver Frequência") }

                Button(
                    onClick = { viewModel.navigateToNotices() },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Ver Avisos") }

                // Botões exclusivos do Teacher
                if (isTeacher) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Ações do professor",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )

                    OutlinedButton(
                        onClick = { viewModel.navigateToAddGrade() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Lançar Nota") }

                    OutlinedButton(
                        onClick = { viewModel.navigateToAddAttendance() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Lançar Frequência") }

                    OutlinedButton(
                        onClick = { viewModel.navigateToAddNotice() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text("Criar Aviso") }
                }
            }
        }
    }
}