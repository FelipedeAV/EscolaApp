package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchAttendanceItemRequest(
    @SerialName("student_id") val studentId: Int,
    @SerialName("is_present") val isPresent: Boolean,
    val note: String? = null,
)