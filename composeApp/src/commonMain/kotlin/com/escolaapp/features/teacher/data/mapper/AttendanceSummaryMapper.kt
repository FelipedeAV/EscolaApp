package com.escolaapp.features.teacher.data.mapper

import com.escolaapp.core.data.models.AttendanceSummaryResponse
import com.escolaapp.core.data.models.StudentAttendanceStatusResponse
import com.escolaapp.features.teacher.domain.model.AttendanceSummary
import com.escolaapp.core.domain.model.StudentAttendanceStatus

fun StudentAttendanceStatusResponse.toDomain() = StudentAttendanceStatus(
    id = id,
    name = name,
    status = status,
    lastAttendance = lastAttendance,
    absenceNote = absenceNote,
    consecutiveAbsences = consecutiveAbsences,
)

fun AttendanceSummaryResponse.toDomain() = AttendanceSummary(
    classId = classId,
    subject = subject,
    room = room,
    period = period,
    date = date,
    totalStudents = totalStudents,
    presentCount = presentCount,
    absentCount = absentCount,
    pendingCount = pendingCount,
    students = students.map { it.toDomain() },
)