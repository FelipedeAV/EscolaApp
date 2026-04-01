package com.escolaapp

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.repository.AttendanceRepository
import com.escolaapp.data.repository.AuthRepository
import com.escolaapp.data.repository.GradeRepository
import com.escolaapp.data.repository.NoticeRepository
import com.escolaapp.data.repository.StudentRepository
import com.escolaapp.navigation.NavigationViewModel
import com.escolaapp.presentation.attendance.AttendanceViewModel
import com.escolaapp.presentation.dashboard.DashboardViewModel
import com.escolaapp.presentation.grades.GradesViewModel
import com.escolaapp.presentation.login.LoginViewModel
import com.escolaapp.presentation.notices.NoticesViewModel
import org.koin.dsl.module

val appModule = module {

    // ApiClient
    single { ApiClient() }

    // Repositories
    single { AuthRepository(get()) }
    single { StudentRepository(get()) }
    single { GradeRepository(get()) }
    single { AttendanceRepository(get()) }
    single { NoticeRepository(get()) }

    // Navigation
    single { NavigationViewModel() }

    // ViewModels
    factory { LoginViewModel(get(), get()) }
    factory { DashboardViewModel(get(), get()) }
    factory { GradesViewModel(get(), get()) }
    factory { AttendanceViewModel(get(), get()) }
    factory { NoticesViewModel(get(), get()) }
}