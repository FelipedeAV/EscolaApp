package com.escolaapp.domain.model

data class AuthToken(
    val token: String,
    val name: String,
    val guardianId: Int,
)