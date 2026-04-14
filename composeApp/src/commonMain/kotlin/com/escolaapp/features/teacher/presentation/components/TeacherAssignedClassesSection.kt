package com.escolaapp.features.teacher.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escolaapp.features.teacher.domain.model.Class

fun LazyListScope.teacherAssignedClassesSection(
    classes: List<Class>,
    onSeeFullScheduleClick: () -> Unit = {},
) {
    item {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Aulas Atribuídas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    if (classes.isNotEmpty()) {
                        TextButton(onClick = onSeeFullScheduleClick) {
                            Text(
                                text = "Ver Horário\nCompleto",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }

                if (classes.isEmpty()) {
                    Text(
                        text = "Nenhuma aula atribuída no momento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                } else {
                    classes.forEachIndexed { index, schoolClass ->
                        if (index > 0) {
                            Spacer(Modifier.size(12.dp))
                        }
                        AssignedClassItem(schoolClass = schoolClass)
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignedClassItem(schoolClass: Class) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "📚", fontSize = 20.sp)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = schoolClass.subject,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "${schoolClass.schedule} — ${schoolClass.room}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = "›",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun TeacherAssignedClassesSectionPreview() {
    MaterialTheme {
        LazyColumn {
            teacherAssignedClassesSection(classes = previewAssignedClasses)
        }
    }
}

@Preview
@Composable
private fun TeacherAssignedClassesSectionEmptyPreview() {
    MaterialTheme {
        LazyColumn {
            teacherAssignedClassesSection(classes = emptyList())
        }
    }
}

private val previewAssignedClasses = listOf(
    Class(
        id = 1,
        subject = "Matemática",
        room = "Sala 08",
        schedule = "07:30 - 08:20",
        dayOfWeek = "Quinta-feira",
        teacherId = 1,
    ),
    Class(
        id = 2,
        subject = "Física",
        room = "Laboratório 2",
        schedule = "09:10 - 10:00",
        dayOfWeek = "Quinta-feira",
        teacherId = 1,
    ),
)