package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.models.GradeRequest

interface IGradeRepository {
    suspend fun addGrade(token: String, request: GradeRequest)
}