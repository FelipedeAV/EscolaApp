package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.domain.model.ClassGradeSummary

interface IGradeBookRepository {
    suspend fun getClassGradeSummary(token: String, classId: Int, bimester: Int): ClassGradeSummary
    suspend fun sendBatchGrades(token: String, classId: Int, bimester: Int, grades: Map<Pair<Int, String>, Double>)
}