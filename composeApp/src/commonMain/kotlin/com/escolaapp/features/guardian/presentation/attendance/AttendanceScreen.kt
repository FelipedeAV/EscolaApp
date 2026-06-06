package com.escolaapp.features.guardian.presentation.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import com.escolaapp.shared.components.AppErrorState
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.components.AppTopBar
import org.koin.compose.koinInject

data class AttendanceScreen(
    val token: String,
    val studentId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: AttendanceViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadAttendance(token, studentId)
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Frequência",
                    onBackClick = { viewModel.navigateBack() },
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

            val total = uiState.attendances.size
            val presents = uiState.attendances.count { it.isPresent }
            val percent = if (total > 0) (presents * 100) / total else 0

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {

            Text(
                text = "Presença: $percent%",
                style = MaterialTheme.typography.titleMedium,
                color = if (percent >= 75)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.attendances) { attendance ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = attendance.date,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                text = if (attendance.isPresent) "Presente" else "Falta",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (attendance.isPresent)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }
            }
            }
        }
    }
}