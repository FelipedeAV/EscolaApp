package com.escolaapp.features.teacher

import com.escolaapp.core.i18n.AppStrings
import com.escolaapp.features.teacher.data.repository.AttendanceSummaryRepository
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import com.escolaapp.features.teacher.presentation.addattendance.AddAttendanceViewModel
import com.escolaapp.features.teacher.presentation.attendance.AttendanceCallViewModel
import com.escolaapp.features.teacher.presentation.classlist.ClassListViewModel
import com.escolaapp.features.teacher.presentation.dashboard.TeacherDashboardViewModel
import com.escolaapp.features.teacher.presentation.grade.AddGradeViewModel
import com.escolaapp.features.teacher.presentation.gradebook.GradeBookViewModel
import com.escolaapp.features.teacher.presentation.notice.AddNoticeViewModel
import org.koin.dsl.module

val teacherModule = module {
    // Repositories
    single { ClassRepository(get()) }
    single { AttendanceSummaryRepository(get()) }
    single { GradeBookRepository(get()) }

    // ViewModels
    factory { AddGradeViewModel(strings = get(), apiClient = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { AddAttendanceViewModel(strings = get(), apiClient = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { AddNoticeViewModel(strings = get(), apiClient = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { TeacherDashboardViewModel(classRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { (classId: Int) -> AttendanceCallViewModel(strings = get(), repository = get(), appEventNavigator = get(), sessionManager = get(), classId = classId) }
    factory { (classId: Int, bimester: Int) -> GradeBookViewModel(strings = get(), repository = get(), appEventNavigator = get(), sessionManager = get(), classId = classId, initialBimester = bimester) }
    factory { (teacherId: Int) -> ClassListViewModel(classRepository = get(), appEventNavigator = get(), sessionManager = get(), teacherId = teacherId) }
}