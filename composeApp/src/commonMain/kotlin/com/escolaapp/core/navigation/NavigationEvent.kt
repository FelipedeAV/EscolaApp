package com.escolaapp.core.navigation

import com.escolaapp.features.teacher.domain.model.ClassListMode

sealed class NavigationEvent {
    data class ToDashboard(
        val token: String,
        val userId: Int,
        val name: String,
        val email: String,
        val role: String,
    ) : NavigationEvent()

    data class ToGrades(
        val token: String,
        val studentId: Int,
    ) : NavigationEvent()

    data class ToAttendance(
        val token: String,
        val studentId: Int,
    ) : NavigationEvent()

    data class ToNotices(
        val token: String,
    ) : NavigationEvent()

    data class ToAddGrade(
        val token: String,
    ) : NavigationEvent()

    data class ToAddAttendance(
        val token: String,
    ) : NavigationEvent()

    data class ToAddNotice(
        val token: String,
    ) : NavigationEvent()

    data class ToProfile(
        val token: String,
        val userId: Int,
        val name: String,
        val email: String,
        val role: String,
    ) : NavigationEvent()

    data class ToProfileSettings(
        val token: String,
        val userId: Int,
    ) : NavigationEvent()

    data class ToTeacherDashboard(
        val token: String,
        val userId: Int,
        val name: String,
        val email: String,
        val role: String,
    ) : NavigationEvent()

    data class ToAttendanceCall(
        val token: String,
        val classId: Int,
    ) : NavigationEvent()

    data class ToGradeBook(
        val token: String,
        val classId: Int,
    ) : NavigationEvent()

    data class ToClassList(
        val token: String,
        val teacherId: Int,
        val name: String,
        val email: String,
        val role: String,
        val mode: ClassListMode,
    ) : NavigationEvent()

    data class GoToStudentRegistration(
        val token: String,
    ) : NavigationEvent()

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