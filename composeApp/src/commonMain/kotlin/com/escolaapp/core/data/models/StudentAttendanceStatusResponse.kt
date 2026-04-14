package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentAttendanceStatusResponse(
    val id: Int,
    val name: String,
    val status: String,
    @SerialName("last_attendance") val lastAttendance: String? = null,
    @SerialName("absence_note") val absenceNote: String? = null,
    @SerialName("consecutive_absences") val consecutiveAbsences: Int = 0,
)