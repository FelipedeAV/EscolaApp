package com.escolaapp.features.teacher.data.mapper

import com.escolaapp.core.data.models.GradeResponse
import com.escolaapp.features.teacher.domain.model.Grade

fun GradeResponse.toDomain() = Grade(
    id = id,
    studentId = studentId,
    subject = subject,
    bimester = bimester,
    value = value,
)