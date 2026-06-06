package com.escolaapp.features.teacher.presentation.addattendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.shared.components.AppTopBar
import org.koin.compose.koinInject

class AddAttendanceScreen() : Screen {

    @Composable
    override fun Content() {
        val s = LocalAppStrings.current
        val viewModel: AddAttendanceViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        var studentId by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var isPresent by remember { mutableStateOf(true) }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = s.teacher.addAttendanceTitle,
                    onBackClick = { viewModel.navigateBack() },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {

            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text(s.teacher.studentIdLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text(s.teacher.dateLabel) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = isPresent,
                    onCheckedChange = { isPresent = it },
                )
                Text(s.teacher.isPresentLabel)
            }

            uiState.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            uiState.success?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.addAttendance(
                        studentId = studentId.toIntOrNull() ?: 0,
                        date = date,
                        isPresent = isPresent,
                    )
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.isLoading) CircularProgressIndicator()
                else Text(s.teacher.saveAttendance)
            }
            }
        }
    }
}