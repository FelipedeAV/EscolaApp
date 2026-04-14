package com.escolaapp.features.teacher.data.mapper

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.data.gateway.dto.ClassResponse

fun ClassResponse.toDomain() = Class(
    id = id,
    subject = subject,
    room = room,
    schedule = schedule,
    dayOfWeek = dayOfWeek,
    teacherId = teacherId,
    students = students.map { it.toDomain() },
)