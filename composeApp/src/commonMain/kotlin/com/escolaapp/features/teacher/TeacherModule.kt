package com.escolaapp.features.teacher

import com.escolaapp.features.teacher.data.repository.AttendanceSummaryRepository
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.data.repository.GradeBookRepository
import com.escolaapp.features.teacher.domain.model.ClassListMode
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
    factory { AddGradeViewModel(get(), get()) }
    factory { AddAttendanceViewModel(get(), get()) }
    factory { AddNoticeViewModel(get(), get()) }
    factory { (token: String, teacherId: Int) ->
        TeacherDashboardViewModel(
            classRepository = get(),
            navigationViewModel = get(),
            token = token,
            teacherId = teacherId,
        )
    }
    factory { AttendanceCallViewModel(repository = get(), navigationViewModel = get()) }
    factory { GradeBookViewModel(repository = get(), navigationViewModel = get()) }
    factory { ClassListViewModel(classRepository = get(), navigationViewModel = get()) }
}