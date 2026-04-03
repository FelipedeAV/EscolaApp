package com.escolaapp.data.mapper

import com.escolaapp.data.gateway.dto.ClassResponse
import com.escolaapp.domain.model.Class

fun ClassResponse.toDomain() = Class(
    id = id,
    subject = subject,
    room = room,
    schedule = schedule,
    dayOfWeek = dayOfWeek,
    teacherId = teacherId,
    students = students.map { it.toDomain() },
)