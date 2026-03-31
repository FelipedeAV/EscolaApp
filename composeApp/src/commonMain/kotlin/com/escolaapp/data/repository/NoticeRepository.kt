package com.escolaapp.data.repository

import com.escolaapp.data.gateway.ApiClient
import com.escolaapp.data.mapper.toDomain
import com.escolaapp.domain.model.Notice

class NoticeRepository(private val apiClient: ApiClient) {

    suspend fun getNotices(token: String): List<Notice> =
        apiClient.getNotices(token).map { dto ->
            dto.toDomain()
        }
}