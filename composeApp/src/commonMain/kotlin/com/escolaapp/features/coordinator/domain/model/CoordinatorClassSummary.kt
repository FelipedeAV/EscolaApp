package com.escolaapp.features.coordinator.domain.model

import com.escolaapp.core.domain.model.ClassStatus

data class CoordinatorClassSummary(
    val id: Int,
    val name: String,
    val subject: String,
    val period: String,
    val room: String,
    val status: ClassStatus,
    val studentCount: Int,
    val subjectCount: Int,
    val teacherName: String,
)