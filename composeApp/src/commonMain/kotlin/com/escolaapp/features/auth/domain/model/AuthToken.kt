package com.escolaapp.features.auth.domain.model

data class AuthToken(
    val token: String,
    val name: String,
    val userId: Int,
    val role: String,
)