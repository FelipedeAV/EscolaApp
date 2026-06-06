package com.escolaapp.core.data.models

import com.escolaapp.core.domain.model.Role
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val role: Role,
)