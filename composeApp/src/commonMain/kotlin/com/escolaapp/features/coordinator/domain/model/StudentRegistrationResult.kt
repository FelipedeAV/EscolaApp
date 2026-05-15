package com.escolaapp.features.coordinator.domain.model

data class StudentRegistrationResult(
    val studentId: Int,
    val guardianUserId: Int,
    val fullName: String,
    val academicEmail: String,
    val guardianName: String,
    val guardianEmail: String
)