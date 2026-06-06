package com.escolaapp.features.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.core.i18n.LocalLyricist
import com.escolaapp.core.session.SessionManager
import org.koin.compose.koinInject

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: LoginViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        val s = LocalAppStrings.current

        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Scaffold(
            topBar = { },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = s.common.appName,
                    style = MaterialTheme.typography.headlineMedium,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = s.login.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(s.login.emailLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(s.login.passwordLabel) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(8.dp))

                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Button(
                    onClick = { viewModel.login(email, password) },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(s.login.signInButton)
                    }
                }

                Spacer(Modifier.height(24.dp))

                LanguageToggle()
            }
        }
    }
}

@Composable
private fun LanguageToggle() {
    val lyricist = LocalLyricist.current
    val sessionManager: SessionManager = koinInject()
    val isPt = lyricist.languageTag == "pt"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "🇧🇷",
            style = MaterialTheme.typography.bodyLarge,
            alpha = if (isPt) 1f else 0.4f,
        )
        Spacer(Modifier.width(8.dp))
        OutlinedButton(
            onClick = {
                val newLang = if (isPt) "en" else "pt"
                lyricist.languageTag = newLang
                sessionManager.languageTag = newLang
            },
        ) {
            Text(if (isPt) "EN" else "PT")
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "🇺🇸",
            style = MaterialTheme.typography.bodyLarge,
            alpha = if (isPt) 0.4f else 1f,
        )
    }
}