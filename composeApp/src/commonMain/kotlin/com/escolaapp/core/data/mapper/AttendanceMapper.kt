package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.AttendanceResponse
import com.escolaapp.core.domain.model.Attendance

fun AttendanceResponse.toDomain() = Attendance(
    id = id,
    studentId = studentId,
    date = date,
    isPresent = isPresent,
)
