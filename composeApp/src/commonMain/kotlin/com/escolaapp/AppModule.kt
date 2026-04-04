package com.escolaapp

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.repository.AttendanceRepository
import com.escolaapp.data.repository.AttendanceSummaryRepository
import com.escolaapp.data.repository.AuthRepository
import com.escolaapp.data.repository.ClassRepository
import com.escolaapp.data.repository.GradeRepository
import com.escolaapp.data.repository.NoticeRepository
import com.escolaapp.data.repository.StudentRepository
import com.escolaapp.data.repository.UserRepository
import com.escolaapp.navigation.NavigationViewModel
import com.escolaapp.presentation.attendance.AttendanceViewModel
import com.escolaapp.presentation.dashboard.DashboardViewModel
import com.escolaapp.presentation.dashboard.TeacherDashboardViewModel
import com.escolaapp.presentation.grades.GradesViewModel
import com.escolaapp.presentation.login.LoginViewModel
import com.escolaapp.presentation.notices.NoticesViewModel
import com.escolaapp.presentation.profile.ProfileSettingsViewModel
import com.escolaapp.presentation.profile.ProfileViewModel
import com.escolaapp.presentation.teacher.AddAttendanceViewModel
import com.escolaapp.presentation.teacher.AddGradeViewModel
import com.escolaapp.presentation.teacher.AddNoticeViewModel
import com.escolaapp.presentation.teacher.AttendanceCallViewModel
import org.koin.dsl.module

val appModule = module {

    // ApiClient
    single { ApiClient() }

    // Repositories
    single { AuthRepository(get()) }
    single { StudentRepository(get()) }
    single { GradeRepository(get()) }
    single { AttendanceRepository(get()) }
    single { AttendanceSummaryRepository(get()) }
    single { NoticeRepository(get()) }
    single { UserRepository(get()) }
    single { ClassRepository(get()) }

    // Navigation
    single { NavigationViewModel() }

    // ViewModels
    factory { LoginViewModel(get(), get()) }
    factory { DashboardViewModel(get(), get()) }
    factory { GradesViewModel(get(), get()) }
    factory { AttendanceViewModel(get(), get()) }
    factory { NoticesViewModel(get(), get()) }
    factory { ProfileViewModel(get(), get()) }
    factory { ProfileSettingsViewModel(get(), get()) }

    // Teacher ViewModels
    factory { AddGradeViewModel(get(), get()) }
    factory { AddAttendanceViewModel(get(), get()) }
    factory { AttendanceCallViewModel(get(), get()) }
    factory { AddNoticeViewModel(get(), get()) }
    factory { TeacherDashboardViewModel(get(), get()) }
}