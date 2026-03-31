package com.escolaapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val name: String,
    @SerialName("guardian_id") val guardianId: Int,
)