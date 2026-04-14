package com.escolaapp.features.teacher.domain.model

import com.escolaapp.core.domain.model.StudentAttendanceStatus

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