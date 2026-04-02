package com.escolaapp.data.mapper

import com.escolaapp.data.models.LoginResponse
import com.escolaapp.domain.model.AuthToken

fun LoginResponse.toDomain() = AuthToken(
    token = token,
    name = name,
    userId = userId,
    role = role,
)