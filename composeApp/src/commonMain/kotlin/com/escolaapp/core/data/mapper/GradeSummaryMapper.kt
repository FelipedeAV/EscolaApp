package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.ClassGradeSummaryResponse
import com.escolaapp.core.data.models.GradeItemResponse
import com.escolaapp.core.data.models.StudentGradeSummaryResponse
import com.escolaapp.features.teacher.domain.model.ClassGradeSummary
import com.escolaapp.features.teacher.domain.model.GradeItem
import com.escolaapp.core.domain.model.StudentGradeSummary

fun GradeItemResponse.toDomain() = GradeItem(
    gradeId = gradeId,
    evaluation = evaluation,
    value = value,
)

fun StudentGradeSummaryResponse.toDomain() = StudentGradeSummary(
    id = id,
    name = name,
    average = average,
    grades = grades.map { it.toDomain() },
)

fun ClassGradeSummaryResponse.toDomain() = ClassGradeSummary(
    classId = classId,
    subject = subject,
    bimester = bimester,
    evaluations = evaluations,
    students = students.map { it.toDomain() },
)