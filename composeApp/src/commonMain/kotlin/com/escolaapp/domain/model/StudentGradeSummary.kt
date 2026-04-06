package com.escolaapp.domain.model

data class StudentGradeSummary(
    val id: Int,
    val name: String,
    val average: Double? = null,
    val grades: List<GradeItem>,
)