package com.escolaapp.features.coordinator.data.mapper

import com.escolaapp.features.coordinator.data.model.CoordinatorDashboardResponse
import com.escolaapp.features.coordinator.data.model.QuickActionResponse
import com.escolaapp.features.coordinator.data.model.RecentActivityResponse
import com.escolaapp.features.coordinator.data.model.SemesterStatsResponse
import com.escolaapp.features.coordinator.domain.model.ActivityType
import com.escolaapp.features.coordinator.domain.model.CoordinatorDashboard
import com.escolaapp.features.coordinator.domain.model.QuickAction
import com.escolaapp.features.coordinator.domain.model.RecentActivity
import com.escolaapp.features.coordinator.domain.model.SemesterStats
import kotlin.time.Instant

fun CoordinatorDashboardResponse.toDomain() = CoordinatorDashboard(
    semesterStats = semesterStats.toDomain(),
    pendingRequestsCount = pendingRequestsCount,
    recentActivities = recentActivities.map { it.toDomain() },
    quickActions = quickActions.map { it.toDomain() },
)

fun SemesterStatsResponse.toDomain() = SemesterStats(
    activeClasses = activeClasses,
    totalTeachers = totalTeachers,
    totalStudents = totalStudents,
    globalAverage = globalAverage,
    classesWithCompleteSchedulePercent = classesWithCompleteSchedulePercent,
    newStudentsThisWeek = newStudentsThisWeek,
)

fun RecentActivityResponse.toDomain() = RecentActivity(
    type = ActivityType.from(type),
    description = description,
    relatedEntity = relatedEntity,
    occurredAt = Instant.parse(occurredAt),
)

fun QuickActionResponse.toDomain() = QuickAction(
    key = key,
    label = label,
    subtitle = subtitle,
    hasPendingAlert = hasPendingAlert,
    alertCount = alertCount,
)