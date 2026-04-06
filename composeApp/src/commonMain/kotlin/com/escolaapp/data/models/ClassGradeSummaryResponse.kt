package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassGradeSummaryResponse(
    @SerialName("class_id") val classId: Int,
    val subject: String,
    val bimester: Int,
    val evaluations: List<String>,
    val students: List<StudentGradeSummaryResponse>,
)