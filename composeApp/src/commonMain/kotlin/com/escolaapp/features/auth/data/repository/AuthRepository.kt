package com.escolaapp.features.auth.data.repository

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.auth.data.mapper.toDomain
import com.escolaapp.features.auth.domain.model.AuthToken

class AuthRepository(private val apiClient: ApiClient) {

    suspend fun login(email: String, password: String): AuthToken =
        apiClient.login(email, password).toDomain()
}