package com.escolaapp.navigation

sealed class NavigationEvent {
    data class ToDashboard(
        val token: String,
        val guardianId: Int,
        val name: String,
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

    object Back : NavigationEvent()
}