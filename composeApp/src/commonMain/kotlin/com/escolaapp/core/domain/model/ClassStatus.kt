package com.escolaapp.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ClassStatus {
    @SerialName("Active")
    ACTIVE,

    @SerialName("Planning")
    PLANNING,

    @SerialName("Inactive")
    INACTIVE
}