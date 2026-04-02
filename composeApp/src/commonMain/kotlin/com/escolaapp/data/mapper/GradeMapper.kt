package com.escolaapp.data.mapper

import com.escolaapp.data.models.GradeResponse
import com.escolaapp.domain.model.Grade

fun GradeResponse.toDomain() = Grade(
    id = id,
    studentId = studentId,
    subject = subject,
    bimester = bimester,
    value = value,
)