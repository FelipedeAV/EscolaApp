package com.escolaapp.domain.model

data class Class(
    val id: Int,
    val subject: String,
    val room: String,
    val schedule: String,
    val dayOfWeek: String,
    val teacherId: Int,
    val students: List<Student> = emptyList(),
)