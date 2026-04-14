package com.escolaapp.core.domain.model

import com.escolaapp.features.teacher.domain.model.GradeItem

data class StudentGradeSummary(
    val id: Int,
    val name: String,
    val average: Double? = null,
    val grades: List<GradeItem>,
)