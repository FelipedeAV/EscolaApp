package com.escolaapp

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.guardian.data.repository.AttendanceRepository
import com.escolaapp.features.teacher.data.repository.AttendanceSummaryRepository
import com.escolaapp.features.auth.data.repository.AuthRepository
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import com.escolaapp.features.guardian.data.repository.GradeRepository
import com.escolaapp.core.data.repository.NoticeRepository
import com.escolaapp.core.data.repository.StudentRepository
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.guardian.presentation.attendance.AttendanceViewModel
import com.escolaapp.features.guardian.presentation.dashboard.DashboardViewModel
import com.escolaapp.features.teacher.presentation.dashboard.TeacherDashboardViewModel
import com.escolaapp.features.guardian.presentation.grades.GradesViewModel
import com.escolaapp.features.auth.presentation.login.LoginViewModel
import com.escolaapp.features.guardian.presentation.notices.NoticesViewModel
import com.escolaapp.shared.presentation.profile.ProfileSettingsViewModel
import com.escolaapp.shared.presentation.profile.ProfileViewModel
import com.escolaapp.features.teacher.presentation.AddAttendanceViewModel
import com.escolaapp.features.teacher.presentation.AddGradeViewModel
import com.escolaapp.features.teacher.presentation.AddNoticeViewModel
import com.escolaapp.features.teacher.presentation.attendance.AttendanceCallViewModel
import com.escolaapp.features.teacher.presentation.classlist.ClassListViewModel
import com.escolaapp.features.teacher.presentation.gradebook.GradeBookViewModel
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
    single { GradeBookRepository(get()) }

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
    factory { GradeBookViewModel(get(), get()) }
    factory { ClassListViewModel(get(), get()) }
}