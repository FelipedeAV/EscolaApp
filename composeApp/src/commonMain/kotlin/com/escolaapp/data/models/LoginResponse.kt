package com.escolaapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val name: String,
    val password: String,
)