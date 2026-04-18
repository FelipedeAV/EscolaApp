package com.escolaapp.features.coordinator.domain.model

data class SemesterStats(
    val activeClasses: Int,
    val totalTeachers: Int,
    val totalStudents: Int,
    val globalAverage: Double,
    val classesWithCompleteSchedulePercent: Double,
    val newStudentsThisWeek: Int,
)