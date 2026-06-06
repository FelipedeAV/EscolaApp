package com.escolaapp.features.teacher.presentation.grade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.core.i18n.LocalAppStrings
import com.escolaapp.shared.components.AppTopBar
import org.koin.compose.koinInject

class AddGradeScreen() : Screen {

    @Composable
    override fun Content() {
        val s = LocalAppStrings.current
        val viewModel: AddGradeViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()

        var studentId by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var bimester by remember { mutableStateOf("") }
        var value by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                AppTopBar(
                    title = s.teacher.addGradeTitle,
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
                value = subject,
                onValueChange = { subject = it },
                label = { Text(s.teacher.subjectLabel) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = bimester,
                onValueChange = { bimester = it },
                label = { Text(s.teacher.bimesterLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(s.teacher.gradeLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

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
                    viewModel.addGrade(
                        studentId = studentId.toIntOrNull() ?: 0,
                        subject = subject,
                        bimester = bimester.toIntOrNull() ?: 0,
                        value = value.toDoubleOrNull() ?: 0.0,
                    )
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) CircularProgressIndicator()
                else Text(s.teacher.saveGrade)
            }
            }
        }
    }
}