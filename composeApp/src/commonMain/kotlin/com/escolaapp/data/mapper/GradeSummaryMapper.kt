package com.escolaapp.data.mapper

import com.escolaapp.data.models.ClassGradeSummaryResponse
import com.escolaapp.data.models.GradeItemResponse
import com.escolaapp.data.models.StudentGradeSummaryResponse
import com.escolaapp.domain.model.ClassGradeSummary
import com.escolaapp.domain.model.GradeItem
import com.escolaapp.domain.model.StudentGradeSummary

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