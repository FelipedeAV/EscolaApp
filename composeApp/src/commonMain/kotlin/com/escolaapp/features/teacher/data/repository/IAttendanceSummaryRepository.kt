package com.escolaapp.features.teacher.data.repository

import com.escolaapp.features.teacher.domain.model.AttendanceSummary

interface IAttendanceSummaryRepository {
    suspend fun getSummary(token: String, classId: Int, date: String): AttendanceSummary
    suspend fun sendBatchAttendance(
        token: String, classId: Int, date: String,
        attendances: Map<Int, Boolean>, notes: Map<Int, String?>,
    )
}