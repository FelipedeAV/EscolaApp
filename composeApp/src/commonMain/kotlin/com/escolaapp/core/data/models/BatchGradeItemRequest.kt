package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchGradeItemRequest(
    @SerialName("student_id") val studentId: Int,
    val evaluation: String,
    val value: Double,
)