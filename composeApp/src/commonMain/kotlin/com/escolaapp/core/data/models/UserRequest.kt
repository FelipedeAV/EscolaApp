package com.escolaapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
)