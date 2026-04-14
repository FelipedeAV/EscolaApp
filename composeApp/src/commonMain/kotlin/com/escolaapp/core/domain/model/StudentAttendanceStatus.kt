package com.escolaapp.core.domain.model

data class StudentAttendanceStatus(
    val id: Int,
    val name: String,
    val status: String,
    val lastAttendance: String? = null,
    val absenceNote: String? = null,
    val consecutiveAbsences: Int = 0,
)