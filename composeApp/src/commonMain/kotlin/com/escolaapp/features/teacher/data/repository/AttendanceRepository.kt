package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.models.AttendanceRequest
import com.escolaapp.core.data.remote.gateway.ApiClient

class AttendanceRepository(private val apiClient: ApiClient) : IAttendanceRepository {

    override suspend fun addAttendance(token: String, request: AttendanceRequest) {
        apiClient.addAttendance(token, request)
    }
}