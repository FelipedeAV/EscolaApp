package com.escolaapp.features.coordinator

import com.escolaapp.features.coordinator.data.repository.CoordinatorRepository
import com.escolaapp.features.coordinator.data.repository.StudentRegistrationRepository
import com.escolaapp.features.coordinator.presentation.dashboard.CoordinatorDashboardViewModel
import com.escolaapp.features.coordinator.presentation.studentRegistration.StudentRegistrationViewModel
import org.koin.dsl.module

val coordinatorModule = module {
    // Repository
    single { CoordinatorRepository(get()) }
    single { StudentRegistrationRepository(get()) }

    // ViewModels
    factory { (token: String) ->
        CoordinatorDashboardViewModel(
            repository = get(),
            token = token,
        )
    }

    factory { (token: String) ->
        StudentRegistrationViewModel(
            repository = get(),
            token = token,
        )
    }
}