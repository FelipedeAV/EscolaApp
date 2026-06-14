package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.GradeResponse
import com.escolaapp.core.domain.model.Grade

fun GradeResponse.toDomain() = Grade(
    id = id,
    studentId = studentId,
    subject = subject,
    bimester = bimester,
    value = value,
)