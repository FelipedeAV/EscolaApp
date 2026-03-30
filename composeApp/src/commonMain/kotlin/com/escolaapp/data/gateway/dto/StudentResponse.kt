package com.escolaapp.data.gateway.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentResponse(
    val id: Int,
    val name: String,
    val classroom: String,
    @SerialName("guardian_id") val guardianId: Int,
)