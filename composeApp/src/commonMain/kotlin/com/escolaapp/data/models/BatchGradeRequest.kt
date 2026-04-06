package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BatchGradeRequest(
    @SerialName("class_id") val classId: Int,
    val bimester: Int,
    val grades: List<BatchGradeItemRequest>,
)