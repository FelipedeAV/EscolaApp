package com.escolaapp.presentation.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.escolaapp.presentation.components.TeacherClassCard
import com.escolaapp.utils.TeacherNavigationBar
import com.escolaapp.utils.TeacherNavigationTab
import org.koin.compose.koinInject
data class ClassListScreen(
    val token: String,
    val teacherId: Int,
    val mode: ClassListMode,
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel: ClassListViewModel = koinInject()
        val uiState by viewModel.uiState.collectAsState()
        var selectedTab by remember { mutableStateOf(TeacherNavigationTab.CLASSES) }
        var selectedClassId by remember { mutableStateOf<Int?>(null) }
        var selectedClassSubject by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            viewModel.loadClasses(token, teacherId)
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

        Scaffold(
            bottomBar = {
                TeacherNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        selectedTab = tab
                        if (tab == TeacherNavigationTab.HOME) {
                            viewModel.navigateBack()
                        }
                    },
                )
            }
        ) { innerPadding ->
            if (mode == ClassListMode.SELECT_ACTION && selectedClassId != null && selectedClassSubject != null) {
                ModalBottomSheet(
                    onDismissRequest = {
                        selectedClassId = null
                        selectedClassSubject = null
                    },
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = selectedClassSubject.orEmpty(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = "Escolha o que deseja acessar para esta turma.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Button(
                            onClick = {
                                val classId = selectedClassId ?: return@Button
                                selectedClassId = null
                                selectedClassSubject = null
                                viewModel.navigateToClass(token, classId, ClassListMode.ATTENDANCE)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Ir para Presença")
                        }
                        Button(
                            onClick = {
                                val classId = selectedClassId ?: return@Button
                                selectedClassId = null
                                selectedClassSubject = null
                                viewModel.navigateToClass(token, classId, ClassListMode.GRADEBOOK)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Ir para Notas")
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            LazyColumn(
                modifier            = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text  = "SEMESTRE 2024.1",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text       = when (mode) {
                            ClassListMode.SELECT_ACTION -> "Turmas"
                            ClassListMode.ATTENDANCE -> "Fazer Chamada"
                            ClassListMode.GRADEBOOK -> "Lançar Notas"
                        },
                        style      = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text  = when (mode) {
                            ClassListMode.SELECT_ACTION -> "Escolha uma turma para continuar"
                            else -> "Selecione uma turma"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = viewModel::onSearchQueryChanged,
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        placeholder = { Text("Buscar turma ou disciplina...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                            focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                    Spacer(Modifier.height(8.dp))
                }

                if (uiState.filteredClasses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = if (uiState.searchQuery.isBlank()) {
                                    "Nenhuma turma disponível no momento."
                                } else {
                                    "Nenhuma turma encontrada para sua busca."
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                } else {
                    items(uiState.filteredClasses) { schoolClass ->
                        TeacherClassCard(
                            subject   = schoolClass.subject,
                            room      = schoolClass.room,
                            schedule  = schoolClass.schedule,
                            dayOfWeek = schoolClass.dayOfWeek,
                            studentCount = schoolClass.students.size,
                            onClick   = {
                                if (mode == ClassListMode.SELECT_ACTION) {
                                    selectedClassId = schoolClass.id
                                    selectedClassSubject = schoolClass.subject
                                } else {
                                    viewModel.navigateToClass(
                                        token   = token,
                                        classId = schoolClass.id,
                                        mode    = mode
                                    )
                                }
                            }
                        )
                    }
                }

                item {
                    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    val bgColor = MaterialTheme.colorScheme.background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRoundRect(
                                    color = borderColor,
                                    style = Stroke(
                                        width = 1.5.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(10.dp.toPx(), 6.dp.toPx())
                                        )
                                    ),
                                    cornerRadius = CornerRadius(16.dp.toPx())
                                )
                            }
                            .background(
                                color = bgColor,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 28.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .border(
                                        width = 1.5.dp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        shape = CircleShape,
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "+",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Light,
                                )
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Solicitar Nova Turma",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Entre em contato com a coordenação para novos horários",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }

                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }
}