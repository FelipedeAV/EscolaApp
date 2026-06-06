package com.escolaapp.features.guardian.presentation.grades

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
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.shared.components.AppErrorState
import com.escolaapp.shared.components.AppLoadingIndicator
import com.escolaapp.shared.components.AppTopBar
import org.koin.compose.koinInject

data class GradesScreen(
    val studentId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: GradesViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        val s = LocalAppStrings.current

        LaunchedEffect(Unit) {
            viewModel.loadGrades(studentId)
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = s.guardian.gradesTitle,
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.grades, key = { it.id }) { grade ->
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
                                    text = s.teacher.bimesterBadge(grade.bimester),
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
}