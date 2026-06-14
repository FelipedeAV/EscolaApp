package com.escolaapp.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    @SerialName("guardian")
    GUARDIAN,
    
    @SerialName("teacher")
    TEACHER,
    
    @SerialName("coordinator")
    COORDINATOR
}