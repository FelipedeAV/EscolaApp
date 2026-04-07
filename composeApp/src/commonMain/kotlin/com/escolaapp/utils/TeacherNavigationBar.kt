package com.escolaapp.utils

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ImportContacts
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        NavigationBarItem(
            selected = selectedTab == TeacherNavigationTab.HOME,
            onClick = { onTabSelected(TeacherNavigationTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Início",
                )
            },
            label = { Text("Início") },
        )

        NavigationBarItem(
            selected = selectedTab == TeacherNavigationTab.CLASSES,
            onClick = { onTabSelected(TeacherNavigationTab.CLASSES) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.ImportContacts,
                    contentDescription = "Turmas",
                )
            },
            label = { Text("Turmas") },
        )

        NavigationBarItem(
            selected = selectedTab == TeacherNavigationTab.SETTINGS,
            onClick = { onTabSelected(TeacherNavigationTab.SETTINGS) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.ManageAccounts,
                    contentDescription = "Configurações",
                )
            },
            label = { Text("Configurações") },
        )
    }
}
