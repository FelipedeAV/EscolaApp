package com.escolaapp.features.coordinator.domain.model

data class QuickAction(
    val key: String,
    val label: String,
    val subtitle: String,
    val hasPendingAlert: Boolean,
    val alertCount: Int,
)