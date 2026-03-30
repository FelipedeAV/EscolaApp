package com.escolaapp.domain.model

data class Grade(
    val id: Int,
    val studentId: Int,
    val subject: String,
    val bimester: Int,
    val value: Double,
)