package com.escolaapp.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.features.auth.presentation.login.LoginScreen

@Composable
fun AppNavigation() {
    Navigator(LoginScreen())
}