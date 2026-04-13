package com.escolaapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
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
fun AppActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    containerColor: Color? = null,
    disabledContainerColor: Color? = null,
) {
    val resolvedContainerColor = if (enabled) {
        containerColor ?: MaterialTheme.colorScheme.primary
    } else {
        disabledContainerColor ?: MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    }

    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }

    Box(
        modifier = modifier
            .background(resolvedContainerColor, RoundedCornerShape(8.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor,
                )
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text = text,
                color = contentColor,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview
@Composable
private fun AppActionButtonPreview() {
    MaterialTheme {
        AppActionButton(
            text = "Salvar Notas do Aluno",
            onClick = {},
            icon = Icons.Filled.Save,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun AppActionButtonDisabledPreview() {
    MaterialTheme {
        AppActionButton(
            text = "Salvar Notas do Aluno",
            onClick = {},
            enabled = false,
            icon = Icons.Filled.Save,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

