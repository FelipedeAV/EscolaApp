package com.escolaapp.core.domain.model

data class ClassGradeSummary(
    val classId: Int,
    val subject: String,
    val bimester: Int,
    val evaluations: List<String>,
    val students: List<StudentGradeSummary>,
)
