package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoticeResponse(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName("published_at") val publishedAt: String,
)