package com.escolaapp.core

import com.escolaapp.apiBaseUrl
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.data.repository.NoticeRepository
import com.escolaapp.core.data.repository.StudentRepository
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.core.i18n.PtStrings
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.core.session.SessionManager
import com.escolaapp.shared.presentation.profile.ProfileSettingsViewModel
import com.escolaapp.shared.presentation.profile.ProfileViewModel
import org.koin.dsl.module

val coreModule = module {
    // ApiClient
    single { ApiClient(baseUrl = apiBaseUrl()) }

    // Internacionalização
    single<AppStrings> { PtStrings }

    // Sessão
    single { SessionManager() }

    // Repositórios principais
    single { StudentRepository(get()) }
    single { UserRepository(get()) }
    single { NoticeRepository(get()) }

    // Navegação
    single<AppEventNavigator> { NavigationViewModel() }

    // ViewModels compartilhados
    factory { ProfileViewModel(userRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { ProfileSettingsViewModel(userRepository = get(), appEventNavigator = get(), sessionManager = get(), strings = get()) }
}