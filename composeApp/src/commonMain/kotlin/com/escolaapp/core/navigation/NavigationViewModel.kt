package com.escolaapp.core.navigation

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationViewModel : ScreenModel, AppEventNavigator {

    private val _events = MutableSharedFlow<NavigationEvent>()
    override val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    override suspend fun emit(event: NavigationEvent) {
        _events.emit(event)
    }
}