package com.escolaapp.features.coordinator

import com.escolaapp.features.coordinator.data.repository.CoordinatorRepository
import com.escolaapp.features.coordinator.presentation.dashboard.CoordinatorDashboardViewModel
import org.koin.dsl.module

val coordinatorModule = module {
    // Repository
    single { CoordinatorRepository(get()) }

    // ViewModels
    factory { (token: String) ->
        CoordinatorDashboardViewModel(
            repository = get(),
            token = token,
        )
    }
}