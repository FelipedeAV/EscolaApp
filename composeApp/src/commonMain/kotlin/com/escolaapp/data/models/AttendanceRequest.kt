package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequest(
    @SerialName("student_id") val studentId: Int,
    val date: String,
    @SerialName("is_present") val isPresent: Boolean,
)