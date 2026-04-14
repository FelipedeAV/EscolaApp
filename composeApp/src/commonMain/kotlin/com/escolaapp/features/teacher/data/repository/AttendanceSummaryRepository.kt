package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.models.BatchAttendanceItemRequest
import com.escolaapp.core.data.models.BatchAttendanceRequest
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.teacher.domain.model.AttendanceSummary
import com.escolaapp.features.teacher.data.mapper.toDomain

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