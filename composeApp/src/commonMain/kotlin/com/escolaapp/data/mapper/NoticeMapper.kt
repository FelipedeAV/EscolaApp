package com.escolaapp.data.mapper

import com.escolaapp.data.models.NoticeResponse
import com.escolaapp.domain.model.Notice

fun NoticeResponse.toDomain() = Notice(
    id          = id,
    title       = title,
    description = description,
    publishedAt = publishedAt,
)