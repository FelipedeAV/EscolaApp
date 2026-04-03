package com.escolaapp.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import com.escolaapp.presentation.components.AppTopBar
import org.koin.compose.koinInject

data class ProfileScreen(
    val token: String,
    val userId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: ProfileViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadProfile(token, userId)
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = "Meu Perfil",
                    onBackClick = { viewModel.navigateBack() },
                )
            },
        ) { innerPadding ->
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
                return@Scaffold
            }

            uiState.error?.let {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
                return@Scaffold
            }

            val user = uiState.user ?: return@Scaffold

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Text(
                    text = user.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Button(
                    onClick = { viewModel.navigateToSettings(token, userId) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Configurações do Perfil")
                }

                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Deslogar")
                }
            }
        }
    }
}




