package com.escolaapp.features.teacher

import com.escolaapp.features.teacher.data.repository.AttendanceRepository
import com.escolaapp.features.teacher.data.repository.AttendanceSummaryRepository
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import com.escolaapp.features.teacher.data.repository.GradeRepository
import com.escolaapp.features.teacher.data.repository.IAttendanceRepository
import com.escolaapp.features.teacher.data.repository.IAttendanceSummaryRepository
import com.escolaapp.features.teacher.data.repository.IGradeBookRepository
import com.escolaapp.features.teacher.data.repository.IGradeRepository
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
    single<IAttendanceSummaryRepository> { AttendanceSummaryRepository(get()) }
    single<IGradeBookRepository> { GradeBookRepository(get()) }
    single<IGradeRepository> { GradeRepository(get()) }
    single<IAttendanceRepository> { AttendanceRepository(get()) }

    // ViewModels
    factory { AddGradeViewModel(strings = get(), gradeRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { AddAttendanceViewModel(strings = get(), attendanceRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { AddNoticeViewModel(strings = get(), noticeRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { TeacherDashboardViewModel(classRepository = get(), appEventNavigator = get(), sessionManager = get()) }
    factory { (classId: Int) -> AttendanceCallViewModel(strings = get(), repository = get(), appEventNavigator = get(), sessionManager = get(), classId = classId) }
    factory { (classId: Int, bimester: Int) -> GradeBookViewModel(strings = get(), repository = get(), appEventNavigator = get(), sessionManager = get(), classId = classId, initialBimester = bimester) }
    factory { (teacherId: Int) -> ClassListViewModel(classRepository = get(), appEventNavigator = get(), sessionManager = get(), teacherId = teacherId) }
}