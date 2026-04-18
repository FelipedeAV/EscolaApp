package com.escolaapp.features.coordinator.domain.model

enum class ActivityType {
    NEW_SUBJECT,
    ATTENDANCE_DONE,
    NEW_STUDENT,
    GRADE_BATCH;

    companion object {
        fun from(raw: String): ActivityType = when (raw) {
            "new_subject" -> NEW_SUBJECT
            "attendance_done" -> ATTENDANCE_DONE
            "new_student" -> NEW_STUDENT
            "grade_batch" -> GRADE_BATCH
            else -> NEW_STUDENT
        }
    }
}