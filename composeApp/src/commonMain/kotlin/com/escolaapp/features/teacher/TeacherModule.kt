package com.escolaapp.features.teacher

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

    // ViewModels with parameters
    factory { (token: String) ->
        AddGradeViewModel(
            apiClient = get(),
            appEventNavigator = get(),
            token = token,
        )
    }
    factory { (token: String) ->
        AddAttendanceViewModel(
            apiClient = get(),
            appEventNavigator = get(),
            token = token,
        )
    }
    factory { (token: String) ->
        AddNoticeViewModel(
            apiClient = get(),
            appEventNavigator = get(),
            token = token,
        )
    }
    factory { (token: String, teacherId: Int) ->
        TeacherDashboardViewModel(
            classRepository = get(),
            appEventNavigator = get(),
            token = token,
            teacherId = teacherId,
        )
    }
    factory { (token: String, classId: Int) ->
        AttendanceCallViewModel(
            repository = get(),
            appEventNavigator = get(),
            token = token,
            classId = classId,
        )
    }
    factory { (token: String, classId: Int, bimester: Int) ->
        GradeBookViewModel(
            repository = get(),
            appEventNavigator = get(),
            token = token,
            classId = classId,
            initialBimester = bimester,
        )
    }
    factory { (token: String, teacherId: Int) ->
        ClassListViewModel(
            classRepository = get(),
            appEventNavigator = get(),
            token = token,
            teacherId = teacherId,
        )
    }
}