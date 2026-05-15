package com.escolaapp.core.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentRegistrationResponse(
    @SerialName("student_id") val studentId: Int,
    @SerialName("guardian_user_id") val guardianUserId: Int,
    @SerialName("full_name") val fullName: String,
    @SerialName("academic_email") val academicEmail: String,
    @SerialName("guardian_name") val guardianName: String,
    @SerialName("guardian_email") val guardianEmail: String,
)