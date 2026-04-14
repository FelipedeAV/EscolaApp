package com.escolaapp.features.guardian.data.repository

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.guardian.data.mapper.toDomain
import com.escolaapp.features.teacher.data.mapper.toDomain
import com.escolaapp.features.teacher.domain.model.Attendance

class AttendanceRepository(private val apiClient: ApiClient) {

    suspend fun getAttendanceByStudent(token: String, studentId: Int): List<Attendance> =
        apiClient.getAttendanceByStudent(token, studentId).map { dto ->
            dto.toDomain()
        }
}