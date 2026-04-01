package com.escolaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.presentation.attendance.AttendanceScreen
import com.escolaapp.presentation.dashboard.DashboardScreen
import com.escolaapp.presentation.grades.GradesScreen
import com.escolaapp.presentation.notices.NoticesScreen
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NavigationHandler(
    navigator: Navigator,
    events: SharedFlow<NavigationEvent>,
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NavigationEvent.ToDashboard -> navigator.replace(
                    DashboardScreen(
                        token = event.token,
                        guardianId = event.guardianId,
                        name = event.name,
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

                is NavigationEvent.Back -> navigator.pop()
            }
        }
    }
}