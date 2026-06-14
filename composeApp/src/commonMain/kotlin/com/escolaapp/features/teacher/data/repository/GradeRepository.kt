package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.models.GradeRequest
import com.escolaapp.core.data.remote.gateway.ApiClient

class GradeRepository(private val apiClient: ApiClient) {

    suspend fun addGrade(token: String, request: GradeRequest) {
        apiClient.addGrade(token, request)
    }
}