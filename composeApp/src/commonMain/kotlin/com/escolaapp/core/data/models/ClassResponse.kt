package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassResponse(
    val id: Int,
    val subject: String,
    val room: String,
    val period: String = "",
    val schedule: String,
    @SerialName("day_of_week") val dayOfWeek: String,
    @SerialName("teacher_id") val teacherId: Int,
    val students: List<StudentResponse> = emptyList(),
)