package com.escolaapp.data.mapper

import com.escolaapp.data.models.AttendanceSummaryResponse
import com.escolaapp.data.models.StudentAttendanceStatusResponse
import com.escolaapp.domain.model.AttendanceSummary
import com.escolaapp.domain.model.StudentAttendanceStatus

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