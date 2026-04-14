package com.escolaapp.features.guardian.data.repository

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.teacher.data.mapper.toDomain
import com.escolaapp.features.teacher.domain.model.Grade

class GradeRepository(private val apiClient: ApiClient) {

    suspend fun getGradesByStudent(token: String, studentId: Int): List<Grade> =
        apiClient.getGradesByStudent(token, studentId).map { dto ->
            dto.toDomain()
        }
}