package com.escolaapp.features.coordinator.domain.model

enum class ClassStatus {
    ACTIVE, PLANNING, INACTIVE;

    companion object {
        fun from(raw: String): ClassStatus = when (raw) {
            "active" -> ACTIVE
            "planning" -> PLANNING
            else -> INACTIVE
        }
    }
}