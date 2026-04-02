package com.escolaapp.data.mapper

import com.escolaapp.data.models.AttendanceResponse
import com.escolaapp.domain.model.Attendance

fun AttendanceResponse.toDomain() = Attendance(
    id = id,
    studentId = studentId,
    date = date,
    isPresent = isPresent,
)