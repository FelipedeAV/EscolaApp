package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.features.teacher.data.mapper.toDomain

class ClassRepository(private val apiClient: ApiClient) {

    suspend fun getClassesByTeacher(token: String, teacherId: Int): List<Class> =
        apiClient.getClassesByTeacher(token, teacherId).map { it.toDomain() }

    suspend fun getCurrentClass(token: String, teacherId: Int): Class =
        apiClient.getCurrentClass(token, teacherId).toDomain()
}