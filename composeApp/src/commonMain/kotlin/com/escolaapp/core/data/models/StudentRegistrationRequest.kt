package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentRegistrationRequest(
    @SerialName("full_name") val fullName: String,
    @SerialName("birth_date") val birthDate: String,      // "yyyy-MM-dd"
    @SerialName("academic_email") val academicEmail: String,
    val gender: String,
    val address: String,
    @SerialName("guardian_name") val guardianName: String,
    @SerialName("guardian_phone") val guardianPhone: String,
    @SerialName("guardian_email") val guardianEmail: String,
    val notes: String,
)