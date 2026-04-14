package com.escolaapp.features.teacher.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TeacherActionCard(
    icon: ImageVector,
    iconBackgroundColor: Color,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    buttonBackgroundColor: Color,
    buttonTextColor: Color,
    topLabel: String? = null,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (topLabel != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(iconBackgroundColor, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Text(
                        text = topLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(iconBackgroundColor, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(buttonBackgroundColor, RoundedCornerShape(8.dp))
                    .clickable(onClick = onButtonClick)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buttonText,
                    color = buttonTextColor,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TeacherActionCardWithLabelPreview() {
    MaterialTheme {
        TeacherActionCard(
            icon = Icons.Outlined.Class,
            iconBackgroundColor = Color(0xFFFFF3E0),
            title = "Fazer Chamada",
            description = "Sessão Atual: Matemática (Sala 08)",
            buttonText = "Marcar Presença ✓",
            onButtonClick = {},
            buttonBackgroundColor = Color(0xFF1565C0),
            buttonTextColor = Color.White,
            topLabel = "ACESSO RÁPIDO",
        )
    }
}

@Preview
@Composable
private fun TeacherActionCardWithoutLabelPreview() {
    MaterialTheme {
        TeacherActionCard(
            icon = Icons.Outlined.School,
            iconBackgroundColor = Color(0xFFE8F4FD),
            title = "Lançar Notas",
            description = "Selecione uma turma para lançar notas.",
            buttonText = "Abrir Diário de Classe ->",
            onButtonClick = {},
            buttonBackgroundColor = Color(0xFF1565C0),
            buttonTextColor = Color.White,
        )
    }
}