package com.escolaapp.features.coordinator.data.repository

import com.escolaapp.core.data.mapper.toDomain
import com.escolaapp.core.data.models.StudentRegistrationRequest
import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationForm
import com.escolaapp.features.coordinator.domain.model.StudentRegistrationResult

class StudentRegistrationRepository(private val apiClient: ApiClient) {

    suspend fun register(token: String, form: StudentRegistrationForm): StudentRegistrationResult {
        val request = StudentRegistrationRequest(
            fullName = form.fullName,
            birthDate = form.birthDate?.toString() ?: "",
            academicEmail = form.academicEmail,
            gender = form.gender,
            address = form.address,
            guardianName = form.guardianName,
            guardianPhone = form.guardianPhone,
            guardianEmail = form.guardianEmail,
            notes = form.notes,
        )
        return apiClient.registerStudent(token, request).toDomain()
    }
}