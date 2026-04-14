package com.escolaapp.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class NoticeRequest(
    val title: String,
    val description: String,
)