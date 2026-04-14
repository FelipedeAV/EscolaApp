package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceSummaryResponse(
    @SerialName("class_id") val classId: Int,
    val subject: String,
    val room: String,
    val period: String,
    val date: String,
    @SerialName("total_students") val totalStudents: Int,
    @SerialName("present_count") val presentCount: Int,
    @SerialName("absent_count") val absentCount: Int,
    @SerialName("pending_count") val pendingCount: Int,
    val students: List<StudentAttendanceStatusResponse>,
)