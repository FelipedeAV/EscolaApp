package com.escolaapp.features.coordinator.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorClassSummaryResponse(
    val id: Int,
    val name: String,
    val subject: String,
    val period: String,
    val room: String,
    val status: String,
    @SerialName("student_count") val studentCount: Int,
    @SerialName("subject_count") val subjectCount: Int,
    @SerialName("teacher_name") val teacherName: String,
)