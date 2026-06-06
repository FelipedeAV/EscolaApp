package com.escolaapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.core.navigation.NavigationHandler
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.features.auth.presentation.login.LoginScreen
import com.escolaapp.shared.theme.AppTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val navigationViewModel: AppEventNavigator = koinInject()

        AppTheme {
            Navigator(LoginScreen()) { navigator ->
                NavigationHandler(
                    navigator = navigator,
                    events = navigationViewModel.events,
                )
                navigator.saveableState("currentScreen") {
                    navigator.lastItem.Content()
                }
            }
        }
    }
}