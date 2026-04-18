package com.escolaapp.features.coordinator.domain.model

data class CoordinatorDashboard(
    val semesterStats: SemesterStats,
    val pendingRequestsCount: Int,
    val recentActivities: List<RecentActivity>,
    val quickActions: List<QuickAction>,
)