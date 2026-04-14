package com.escolaapp.features.teacher.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class TeacherNavigationTab {
    HOME,
    CLASSES,
    SETTINGS,
}

@Composable
fun TeacherNavigationBar(
    selectedTab: TeacherNavigationTab,
    onTabSelected: (TeacherNavigationTab) -> Unit,
) {
    NavigationBar(
        modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        containerColor = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TeacherNavigationBarItem(
                selected = selectedTab == TeacherNavigationTab.HOME,
                onClick = { onTabSelected(TeacherNavigationTab.HOME) },
                icon = Icons.Outlined.Home,
                label = "Início",
            )

            TeacherNavigationBarItem(
                selected = selectedTab == TeacherNavigationTab.CLASSES,
                onClick = { onTabSelected(TeacherNavigationTab.CLASSES) },
                icon = Icons.Outlined.Class,
                label = "Turmas",
            )

            TeacherNavigationBarItem(
                selected = selectedTab == TeacherNavigationTab.SETTINGS,
                onClick = { onTabSelected(TeacherNavigationTab.SETTINGS) },
                icon = Icons.Outlined.ManageAccounts,
                label = "Configurações",
            )
        }
    }
}

@Composable
private fun TeacherNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (selected) MaterialTheme.colorScheme.primaryContainer
                    else Color.Transparent
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview(name = "Home selecionado")
@Composable
private fun TeacherNavigationBarHomePreview() {
    MaterialTheme {
        TeacherNavigationBar(
            selectedTab = TeacherNavigationTab.HOME,
            onTabSelected = {},
        )
    }
}

@Preview(name = "Turmas selecionado")
@Composable
private fun TeacherNavigationBarClassesPreview() {
    MaterialTheme {
        TeacherNavigationBar(
            selectedTab = TeacherNavigationTab.CLASSES,
            onTabSelected = {},
        )
    }
}

@Preview(name = "Configurações selecionado")
@Composable
private fun TeacherNavigationBarSettingsPreview() {
    MaterialTheme {
        TeacherNavigationBar(
            selectedTab = TeacherNavigationTab.SETTINGS,
            onTabSelected = {},
        )
    }
}