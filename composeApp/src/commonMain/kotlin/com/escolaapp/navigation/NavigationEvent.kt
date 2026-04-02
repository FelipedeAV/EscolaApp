package com.escolaapp.navigation

sealed class NavigationEvent {
    data class ToDashboard(
        val token: String,
        val userId: Int,
        val name: String,
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

    object Back : NavigationEvent()
}