package com.escolaapp.presentation.dashboard

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.escolaapp.presentation.attendance.AttendanceScreen
import com.escolaapp.presentation.grades.GradesScreen
import com.escolaapp.presentation.notices.NoticesScreen
import org.koin.compose.koinInject

data class DashboardScreen(
    val token: String,
    val guardianId: Int,
    val name: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: DashboardViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadStudent(token, guardianId)
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        uiState.error?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Olá, $name",
                style = MaterialTheme.typography.headlineSmall
            )

            uiState.student?.let { student ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Aluno",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = student.classroom,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { navigator.push(GradesScreen(token, guardianId)) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver Notas") }

            Button(
                onClick = { navigator.push(AttendanceScreen(token, guardianId)) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver Frequência") }

            Button(
                onClick = { navigator.push(NoticesScreen(token)) },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ver Avisos") }
        }
    }
}