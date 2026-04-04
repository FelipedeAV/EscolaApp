package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.data.models.BatchAttendanceItemRequest
import com.escolaapp.data.models.BatchAttendanceRequest
import com.escolaapp.domain.model.AttendanceSummary

class AttendanceSummaryRepository(private val apiClient: ApiClient) {

    suspend fun getSummary(token: String, classId: Int, date: String): AttendanceSummary =
        apiClient.getAttendanceSummary(token, classId, date).toDomain()

    suspend fun sendBatchAttendance(
        token: String,
        classId: Int,
        date: String,
        attendances: Map<Int, Boolean>,
        notes: Map<Int, String?>,
    ) {
        val items = attendances.map { (studentId, isPresent) ->
            BatchAttendanceItemRequest(
                studentId = studentId,
                isPresent = isPresent,
                note = notes[studentId],
            )
        }
        apiClient.sendBatchAttendance(
            token = token,
            request = BatchAttendanceRequest(
                classId = classId,
                date = date,
                attendances = items,
            ),
        )
    }
}