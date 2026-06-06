package com.escolaapp.features.guardian

import com.escolaapp.features.guardian.data.repository.AttendanceRepository
import com.escolaapp.features.guardian.data.repository.GradeRepository
import com.escolaapp.features.guardian.presentation.attendance.AttendanceViewModel
import com.escolaapp.features.guardian.presentation.dashboard.DashboardViewModel
import com.escolaapp.features.guardian.presentation.grades.GradesViewModel
import com.escolaapp.features.guardian.presentation.notices.NoticesViewModel
import org.koin.dsl.module

val guardianModule = module {
    // Repositories
    single { AttendanceRepository(get()) }
    single { GradeRepository(get()) }

    // ViewModels
    factory { DashboardViewModel(get(), get(), get(), get()) }
    factory { GradesViewModel(gradeRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { AttendanceViewModel(attendanceRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { NoticesViewModel(noticeRepository = get(), appEventNavigator = get(), sessionManager = get()) }
}