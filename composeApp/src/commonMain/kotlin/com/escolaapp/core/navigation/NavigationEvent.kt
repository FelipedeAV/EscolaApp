package com.escolaapp.core.navigation

import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.core.domain.model.Role

sealed class NavigationEvent {
    data class ToDashboard(val role: Role) : NavigationEvent()

    data class ToGrades(val studentId: Int) : NavigationEvent()

    data class ToAttendance(val studentId: Int) : NavigationEvent()

    data object ToNotices : NavigationEvent()

    data object ToAddGrade : NavigationEvent()

    data object ToAddAttendance : NavigationEvent()

    data object ToAddNotice : NavigationEvent()

    data object ToProfile : NavigationEvent()

    data object ToProfileSettings : NavigationEvent()

    data object ToTeacherDashboard : NavigationEvent()

    data class ToAttendanceCall(val classId: Int) : NavigationEvent()

    data class ToGradeBook(val classId: Int, val bimester: Int = 1) : NavigationEvent()

    data class ToClassList(val teacherId: Int, val mode: ClassListMode) : NavigationEvent()

    data object GoToStudentRegistration : NavigationEvent()

    object ToLogin : NavigationEvent()

    object GoBack : NavigationEvent()

    data object GoToCoordinatorDashboard : NavigationEvent()
    data object GoToClassManagement : NavigationEvent()
    data object GoToSubjectManagement : NavigationEvent()
    data object GoToTeacherManagement : NavigationEvent()
    data object GoToStudentManagement : NavigationEvent()
    data object GoToAddTeacher : NavigationEvent()
    data object GoToNotifications : NavigationEvent()
    data object GoToSettings : NavigationEvent()
}