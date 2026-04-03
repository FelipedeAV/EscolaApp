package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.Class

class ClassRepository(private val apiClient: ApiClient) {

    suspend fun getClassesByTeacher(token: String, teacherId: Int): List<Class> =
        apiClient.getClassesByTeacher(token, teacherId).map { it.toDomain() }

    suspend fun getCurrentClass(token: String, teacherId: Int): Class? =
        try {
            apiClient.getCurrentClass(token, teacherId).toDomain()
        } catch (e: Exception) {
            null
        }
}