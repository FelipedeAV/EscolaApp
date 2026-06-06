package com.escolaapp.features.auth

import com.escolaapp.features.auth.data.repository.AuthRepository
import com.escolaapp.features.auth.presentation.login.LoginViewModel
import org.koin.dsl.module

val authModule = module {
    single { AuthRepository(get()) }

    factory { LoginViewModel(authRepository = get(), appEventNavigator = get(), sessionManager = get()) }
}