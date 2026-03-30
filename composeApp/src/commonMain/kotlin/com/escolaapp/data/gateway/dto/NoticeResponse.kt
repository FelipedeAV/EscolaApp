package com.escolaapp.data.gateway.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeResponse(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("published_at") val publishedAt: String
)