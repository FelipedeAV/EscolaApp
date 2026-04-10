package com.escolaapp.navigation

import com.escolaapp.presentation.teacher.ClassListMode

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

    object ToLogin : NavigationEvent()

    object Back : NavigationEvent()
}