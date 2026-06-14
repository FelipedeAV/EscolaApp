package com.escolaapp.core.domain.model

data class Attendance(
    val id: Int,
    val studentId: Int,
    val date: String,
    val isPresent: Boolean,
)