package com.escolaapp.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.presentation.login.LoginScreen

@Composable
fun AppNavigation() {
    Navigator(LoginScreen())
}