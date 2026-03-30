package com.escolaapp.data.gateway.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val name: String,
    val password: String,
)