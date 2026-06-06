package com.escolaapp.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.escolaapp.core.i18n.LocalAppStrings

enum class AppNavigationTab {
    HOME,
    CLASSES,
    SETTINGS,
}

@Composable
fun AppNavigationBar(
    selectedTab: AppNavigationTab,
    onTabSelected: (AppNavigationTab) -> Unit,
) {
    val s = LocalAppStrings.current
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
            AppNavigationBarItem(
                selected = selectedTab == AppNavigationTab.HOME,
                onClick = { onTabSelected(AppNavigationTab.HOME) },
                icon = Icons.Outlined.Home,
                label = s.common.home,
            )

            AppNavigationBarItem(
                selected = selectedTab == AppNavigationTab.CLASSES,
                onClick = { onTabSelected(AppNavigationTab.CLASSES) },
                icon = Icons.Outlined.Class,
                label = s.common.classes,
            )

            AppNavigationBarItem(
                selected = selectedTab == AppNavigationTab.SETTINGS,
                onClick = { onTabSelected(AppNavigationTab.SETTINGS) },
                icon = Icons.Outlined.ManageAccounts,
                label = s.common.settings,
            )
        }
    }
}

@Composable
private fun AppNavigationBarItem(
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
private fun AppNavigationBarHomePreview() {
    MaterialTheme {
        AppNavigationBar(
            selectedTab = AppNavigationTab.HOME,
            onTabSelected = {},
        )
    }
}

@Preview(name = "Turmas selecionado")
@Composable
private fun AppNavigationBarClassesPreview() {
    MaterialTheme {
        AppNavigationBar(
            selectedTab = AppNavigationTab.CLASSES,
            onTabSelected = {},
        )
    }
}

@Preview(name = "Configurações selecionado")
@Composable
private fun AppNavigationBarSettingsPreview() {
    MaterialTheme {
        AppNavigationBar(
            selectedTab = AppNavigationTab.SETTINGS,
            onTabSelected = {},
        )
    }
}