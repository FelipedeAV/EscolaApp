package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentResponse(
    val id: Int,
    val name: String,
    val classroom: String,
    @SerialName("user_id") val userId: Int,
)