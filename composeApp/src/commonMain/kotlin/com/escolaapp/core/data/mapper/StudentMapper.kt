package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.StudentResponse
import com.escolaapp.core.domain.model.Student

fun StudentResponse.toDomain() = Student(
    id = id,
    name = name,
    classroom = classroom,
    userId = userId,
)