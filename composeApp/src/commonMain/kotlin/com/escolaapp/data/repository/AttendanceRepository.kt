package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.Attendance

class AttendanceRepository(private val apiClient: ApiClient) {

    suspend fun getAttendanceByStudent(token: String, studentId: Int): List<Attendance> =
        apiClient.getAttendanceByStudent(token, studentId).map { dto ->
            dto.toDomain()
        }
}