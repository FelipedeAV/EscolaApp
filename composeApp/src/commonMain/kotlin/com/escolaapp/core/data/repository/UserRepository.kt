package com.escolaapp.core.data.repository

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.data.models.UserRequest
import com.escolaapp.core.domain.model.User

class UserRepository(private val apiClient: ApiClient) {

    suspend fun getUsers(token: String): List<User> =
        apiClient.getUsers(token).map { it.toDomain() }

    suspend fun getUserById(token: String, id: Int): User =
        apiClient.getUserById(token, id).toDomain()

    suspend fun createUser(user: UserRequest): User =
        apiClient.createUser(user).toDomain()

    suspend fun changePassword(
        token: String,
        userId: Int,
        currentPassword: String,
        newPassword: String,
    ) {
        apiClient.changePassword(token, userId, currentPassword, newPassword)
    }
}