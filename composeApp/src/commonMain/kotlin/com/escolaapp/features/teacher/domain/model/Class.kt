package com.escolaapp.features.teacher.domain.model

import com.escolaapp.core.domain.model.Student

data class Class(
    val id: Int,
    val subject: String,
    val room: String,
    val period: String = "",
    val schedule: String,
    val dayOfWeek: String,
    val teacherId: Int,
    val students: List<Student> = emptyList(),
)