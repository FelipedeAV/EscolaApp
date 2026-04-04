package com.escolaapp.domain.model

data class AttendanceSummary(
    val classId: Int,
    val subject: String,
    val room: String,
    val period: String,
    val date: String,
    val totalStudents: Int,
    val presentCount: Int,
    val absentCount: Int,
    val pendingCount: Int,
    val students: List<StudentAttendanceStatus>,
)