package com.escolaapp.features.auth

import com.escolaapp.features.auth.data.repository.AuthRepository
import com.escolaapp.features.auth.presentation.login.LoginViewModel
import org.koin.dsl.module

val authModule = module {
    // Repository
    single { AuthRepository(get()) }

    // ViewModels
    factory { LoginViewModel(get(), get()) }
}