package com.escolaapp.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role {
    @SerialName("Guardian")
    GUARDIAN,
    
    @SerialName("Teacher")
    TEACHER,
    
    @SerialName("Coordinator")
    COORDINATOR
}