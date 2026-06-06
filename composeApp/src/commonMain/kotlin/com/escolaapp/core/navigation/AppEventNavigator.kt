package com.escolaapp.core.navigation

import kotlinx.coroutines.flow.SharedFlow

interface AppEventNavigator {
    val events: SharedFlow<NavigationEvent>
    suspend fun emit(event: NavigationEvent)
}
