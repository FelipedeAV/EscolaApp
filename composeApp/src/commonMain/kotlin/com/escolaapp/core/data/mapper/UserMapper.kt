package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.UserResponse
import com.escolaapp.core.domain.model.User

fun UserResponse.toDomain() = User(
    id = id,
    name = name,
    email = email,
    role = role,
)