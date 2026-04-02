package com.escolaapp.presentation.grades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.koin.compose.koinInject

data class GradesScreen(
    val token: String,
    val studentId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: GradesViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadGrades(token, studentId)
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

        uiState.error?.let {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            return
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = "Notas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.grades) { grade ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text(
                                    text = grade.subject,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = "${grade.bimester}º Bimestre",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Text(
                                text = grade.value.toString(),
                                style = MaterialTheme.typography.titleLarge,
                                color = if (grade.value >= 6.0)
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