package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchAttendanceRequest(
    @SerialName("class_id") val classId: Int,
    val date: String,
    val attendances: List<BatchAttendanceItemRequest>,
)