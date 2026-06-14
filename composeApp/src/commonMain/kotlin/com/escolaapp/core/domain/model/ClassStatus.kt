package com.escolaapp.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClassStatus {
    @SerialName("active") ACTIVE,
    @SerialName("planning") PLANNING,
    @SerialName("inactive") INACTIVE;

    companion object {
        fun from(raw: String): ClassStatus = when (raw) {
            "active" -> ACTIVE
            "planning" -> PLANNING
            else -> INACTIVE
        }
    }
}