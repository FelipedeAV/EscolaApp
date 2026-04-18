package com.escolaapp.features.coordinator.domain.model

import kotlin.time.Instant

data class RecentActivity(
    val type: ActivityType,
    val description: String,
    val relatedEntity: String,
    val occurredAt: Instant,
)