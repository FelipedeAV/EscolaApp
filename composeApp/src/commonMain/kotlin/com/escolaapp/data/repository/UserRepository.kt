package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.data.models.UserRequest
import com.escolaapp.domain.model.User

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