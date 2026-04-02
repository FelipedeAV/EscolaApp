package com.escolaapp.data.mapper

import com.escolaapp.data.models.UserResponse
import com.escolaapp.domain.model.User

fun UserResponse.toDomain() = User(
    id = id,
    name = name,
    email = email,
    role = role,
)