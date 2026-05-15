package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.StudentRegistrationResponse
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationResult

fun StudentRegistrationResponse.toDomain() = StudentRegistrationResult(
    studentId = studentId,
    guardianUserId = guardianUserId,
    fullName = fullName,
    academicEmail = academicEmail,
    guardianName = guardianName,
    guardianEmail = guardianEmail,
)