package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.Student

class StudentRepository(private val apiClient: ApiClient) {

    suspend fun getStudents(token: String): List<Student> =
        apiClient.getStudents(token).map { dto ->
            dto.toDomain()
        }

    suspend fun getStudentById(token: String, id: Int): Student =
        apiClient.getStudentById(token, id).toDomain()
}