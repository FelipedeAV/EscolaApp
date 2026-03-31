package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.AuthToken

class AuthRepository(private val apiClient: ApiClient) {

    suspend fun login(email: String, password: String): AuthToken =
        apiClient.login(email, password).toDomain()
}