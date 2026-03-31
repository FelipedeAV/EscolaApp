package com.escolaapp.data.mapper

import com.escolaapp.data.models.StudentResponse
import com.escolaapp.domain.model.Student

fun StudentResponse.toDomain() = Student(
    id         = id,
    name       = name,
    classroom  = classroom,
    guardianId = guardianId,
)