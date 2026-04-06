package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.data.models.BatchGradeItemRequest
import com.escolaapp.data.models.BatchGradeRequest
import com.escolaapp.domain.model.ClassGradeSummary

class GradeBookRepository(private val apiClient: ApiClient) {

    suspend fun getClassGradeSummary(
        token: String,
        classId: Int,
        bimester: Int,
    ): ClassGradeSummary =
        apiClient.getClassGradeSummary(token, classId, bimester).toDomain()

    suspend fun sendBatchGrades(
        token: String,
        classId: Int,
        bimester: Int,
        grades: Map<Pair<Int, String>, Double>,
    ) {
        val items = grades.map { (key, value) ->
            BatchGradeItemRequest(
                studentId = key.first,
                evaluation = key.second,
                value = value,
            )
        }
        apiClient.sendBatchGrades(
            token = token,
            request = BatchGradeRequest(
                classId = classId,
                bimester = bimester,
                grades = items,
            )
        )
    }
}