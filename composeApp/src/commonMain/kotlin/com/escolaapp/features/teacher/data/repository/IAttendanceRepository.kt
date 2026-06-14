package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.models.AttendanceRequest

interface IAttendanceRepository {
    suspend fun addAttendance(token: String, request: AttendanceRequest)
}