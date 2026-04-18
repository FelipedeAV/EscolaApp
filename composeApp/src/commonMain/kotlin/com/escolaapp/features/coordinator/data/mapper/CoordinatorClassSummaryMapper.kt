package com.escolaapp.features.coordinator.data.mapper

import com.escolaapp.features.coordinator.data.model.CoordinatorClassSummaryResponse
import com.escolaapp.features.coordinator.domain.model.ClassStatus
import com.escolaapp.features.coordinator.domain.model.CoordinatorClassSummary

fun CoordinatorClassSummaryResponse.toDomain() = CoordinatorClassSummary(
    id = id,
    name = name,
    subject = subject,
    period = period,
    room = room,
    status = ClassStatus.from(status),
    studentCount = studentCount,
    subjectCount = subjectCount,
    teacherName = teacherName,
)