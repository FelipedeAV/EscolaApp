package com.escolaapp.core.data.mapper

import com.escolaapp.core.data.models.NoticeResponse
import com.escolaapp.core.domain.model.Notice

fun NoticeResponse.toDomain() = Notice(
    id = id,
    title = title,
    description = description,
    publishedAt = publishedAt,
)