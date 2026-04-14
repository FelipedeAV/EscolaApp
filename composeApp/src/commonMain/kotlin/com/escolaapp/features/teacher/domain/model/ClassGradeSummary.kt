package com.escolaapp.features.teacher.domain.model

import com.escolaapp.core.domain.model.StudentGradeSummary

data class ClassGradeSummary(
    val classId: Int,
    val subject: String,
    val bimester: Int,
    val evaluations: List<String>,
    val students: List<StudentGradeSummary>,
)