package com.escolaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.presentation.attendance.AttendanceScreen
import com.escolaapp.presentation.dashboard.DashboardScreen
import com.escolaapp.presentation.grades.GradesScreen
import com.escolaapp.presentation.login.LoginScreen
import com.escolaapp.presentation.notices.NoticesScreen
import com.escolaapp.presentation.teacher.AddAttendanceScreen
import com.escolaapp.presentation.teacher.AddGradeScreen
import com.escolaapp.presentation.teacher.AddNoticeScreen
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NavigationHandler(
    navigator: Navigator,
    events: SharedFlow<NavigationEvent>
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NavigationEvent.ToDashboard -> navigator.replace(
                    DashboardScreen(
                        token = event.token,
                        userId = event.userId,
                        name = event.name,
                        role = event.role,
                    )
                )

                is NavigationEvent.ToGrades -> navigator.push(
                    GradesScreen(
                        token = event.token,
                        studentId = event.studentId,
                    )
                )

                is NavigationEvent.ToAttendance -> navigator.push(
                    AttendanceScreen(
                        token = event.token,
                        studentId = event.studentId,
                    )
                )

                is NavigationEvent.ToNotices -> navigator.push(
                    NoticesScreen(token = event.token)
                )

                is NavigationEvent.ToAddGrade -> navigator.push(
                    AddGradeScreen(token = event.token)
                )

                is NavigationEvent.ToAddAttendance -> navigator.push(
                    AddAttendanceScreen(token = event.token)
                )

                is NavigationEvent.ToAddNotice -> navigator.push(
                    AddNoticeScreen(token = event.token)
                )

                is NavigationEvent.ToProfile -> navigator.push(
                    com.escolaapp.presentation.profile.ProfileScreen(
                        token = event.token,
                        userId = event.userId,
                    )
                )

                is NavigationEvent.ToProfileSettings -> navigator.push(
                    com.escolaapp.presentation.profile.ProfileSettingsScreen(
                        token = event.token,
                        userId = event.userId,
                    )
                )

                is NavigationEvent.ToLogin -> navigator.replace(LoginScreen())

                is NavigationEvent.Back -> navigator.pop()
            }
        }
    }
}