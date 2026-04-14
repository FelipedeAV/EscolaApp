package com.escolaapp.features.guardian.data.mapper

import com.escolaapp.core.data.models.AttendanceResponse
import com.escolaapp.features.teacher.domain.model.Attendance

fun AttendanceResponse.toDomain() = Attendance(
    id = id,
    studentId = studentId,
    date = date,
    isPresent = isPresent,
)