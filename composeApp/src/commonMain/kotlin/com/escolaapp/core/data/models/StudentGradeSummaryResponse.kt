package com.escolaapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class StudentGradeSummaryResponse(
    val id: Int,
    val name: String,
    val average: Double? = null,
    val grades: List<GradeItemResponse>,
)