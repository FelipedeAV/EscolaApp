package com.escolaapp.features.auth.domain.model

import com.escolaapp.core.domain.model.Role

data class AuthToken(
    val token: String,
    val name: String,
    val userId: Int,
    val role: Role,
)