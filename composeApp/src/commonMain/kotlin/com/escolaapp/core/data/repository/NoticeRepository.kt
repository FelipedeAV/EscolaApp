package com.escolaapp.core.data.repository

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.core.domain.model.Notice

class NoticeRepository(private val apiClient: ApiClient) {

    suspend fun getNotices(token: String): List<Notice> =
        apiClient.getNotices(token).map { dto ->
            dto.toDomain()
        }
}