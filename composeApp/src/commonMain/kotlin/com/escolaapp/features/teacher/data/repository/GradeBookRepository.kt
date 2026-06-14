package com.escolaapp.features.teacher.data.repository

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.core.data.models.BatchGradeItemRequest
import com.escolaapp.core.data.models.BatchGradeRequest
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.domain.model.ClassGradeSummary

class GradeBookRepository(private val apiClient: ApiClient) : IGradeBookRepository {

    override suspend fun getClassGradeSummary(
        token: String,
        classId: Int,
        bimester: Int,
    ): ClassGradeSummary =
        apiClient.getClassGradeSummary(token, classId, bimester).toDomain()

    override suspend fun sendBatchGrades(
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