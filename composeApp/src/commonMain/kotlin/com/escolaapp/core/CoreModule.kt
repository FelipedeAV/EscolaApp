package com.escolaapp.core

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.data.repository.NoticeRepository
import com.escolaapp.core.data.repository.StudentRepository
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.shared.presentation.profile.ProfileSettingsViewModel
import com.escolaapp.shared.presentation.profile.ProfileViewModel
import org.koin.dsl.module

val coreModule = module {
    // ApiClient
    single { ApiClient() }

    // Core Repositories
    single { StudentRepository(get()) }
    single { UserRepository(get()) }
    single { NoticeRepository(get()) }

    // Navigation
    single { NavigationViewModel() }

    // Shared ViewModels
    factory { (token: String, userId: Int) ->
        ProfileViewModel(
            userRepository = get(),
            navigationViewModel = get(),
            token = token,
            userId = userId,
        )
    }
    factory { (token: String, userId: Int) ->
        ProfileSettingsViewModel(
            userRepository = get(),
            navigationViewModel = get(),
            token = token,
            userId = userId,
        )
    }
}