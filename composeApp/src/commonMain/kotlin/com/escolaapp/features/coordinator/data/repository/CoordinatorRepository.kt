package com.escolaapp.features.coordinator.data.repository

import com.escolaapp.core.data.remote.gateway.ApiClient
import com.escolaapp.features.coordinator.data.mapper.toDomain
import com.escolaapp.features.coordinator.domain.model.CoordinatorClassSummary
import com.escolaapp.features.coordinator.domain.model.CoordinatorDashboard

class CoordinatorRepository(private val apiClient: ApiClient) {

    suspend fun getDashboard(token: String): CoordinatorDashboard =
        apiClient.getCoordinatorDashboard(token).toDomain()

    suspend fun getClassSummaries(token: String): List<CoordinatorClassSummary> =
        apiClient.getCoordinatorClasses(token).map { it.toDomain() }
}