package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GradeItemResponse(
    @SerialName("grade_id") val gradeId: Int? = null,
    val evaluation: String,
    val value: Double? = null,
)