package com.escolaapp.core.data.models

import com.escolaapp.core.domain.model.Role
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val name: String,
    @SerialName("user_id") val userId: Int,
    val role: Role,
)