package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GradeRequest(
    @SerialName("student_id") val studentId: Int,
    val subject: String,
    val bimester: Int,
    val value: Double,
)