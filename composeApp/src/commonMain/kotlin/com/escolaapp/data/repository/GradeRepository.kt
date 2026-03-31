package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.Grade

class GradeRepository(private val apiClient: ApiClient) {

    suspend fun getGradesByStudent(token: String, studentId: Int): List<Grade> =
        apiClient.getGradesByStudent(token, studentId).map { dto ->
            dto.toDomain()
        }
}