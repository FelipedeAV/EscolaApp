package com.escolaapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.navigation.NavigationHandler
import com.escolaapp.navigation.NavigationViewModel
import com.escolaapp.presentation.login.LoginScreen
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        val navigationViewModel: NavigationViewModel = koinInject()

        MaterialTheme {
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