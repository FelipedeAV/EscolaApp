package com.escolaapp.features.auth.data.mapper

import com.escolaapp.core.data.models.LoginResponse
import com.escolaapp.features.auth.domain.model.AuthToken

fun LoginResponse.toDomain() = AuthToken(
    token = token,
    name = name,
    userId = userId,
    role = role,
)