package com.escolaapp.features.coordinator.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoordinatorDashboardResponse(
    @SerialName("semester_stats") val semesterStats: SemesterStatsResponse,
    @SerialName("pending_requests_count") val pendingRequestsCount: Int,
    @SerialName("recent_activities") val recentActivities: List<RecentActivityResponse>,
    @SerialName("quick_actions") val quickActions: List<QuickActionResponse>,
)

@Serializable
data class SemesterStatsResponse(
    @SerialName("active_classes") val activeClasses: Int,
    @SerialName("total_teachers") val totalTeachers: Int,
    @SerialName("total_students") val totalStudents: Int,
    @SerialName("global_average") val globalAverage: Double,
    @SerialName("classes_with_complete_schedule_percent") val classesWithCompleteSchedulePercent: Double,
    @SerialName("new_students_this_week") val newStudentsThisWeek: Int,
)

@Serializable
data class RecentActivityResponse(
    val type: String,
    val description: String,
    @SerialName("related_entity") val relatedEntity: String,
    @SerialName("occurred_at") val occurredAt: String,   // ISO-8601
)

@Serializable
data class QuickActionResponse(
    val key: String,
    val label: String,
    val subtitle: String,
    @SerialName("has_pending_alert") val hasPendingAlert: Boolean,
    @SerialName("alert_count") val alertCount: Int,
)