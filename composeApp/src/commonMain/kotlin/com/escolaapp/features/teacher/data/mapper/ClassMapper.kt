package com.escolaapp.features.teacher.data.mapper

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.core.data.models.ClassResponse

fun ClassResponse.toDomain() = Class(
    id = id,
    subject = subject,
    room = room,
    period = period,
    schedule = schedule,
    dayOfWeek = dayOfWeek,
    teacherId = teacherId,
    students = students.map { it.toDomain() },
)