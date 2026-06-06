package com.escolaapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import com.escolaapp.features.guardian.presentation.attendance.AttendanceScreen
import com.escolaapp.features.guardian.presentation.dashboard.DashboardScreen
import com.escolaapp.features.teacher.presentation.dashboard.TeacherDashboardScreen
import com.escolaapp.features.coordinator.presentation.dashboard.CoordinatorDashboardScreen
import com.escolaapp.features.guardian.presentation.grades.GradesScreen
import com.escolaapp.features.auth.presentation.login.LoginScreen
import com.escolaapp.features.coordinator.presentation.studentRegistration.StudentRegistrationScreen
import com.escolaapp.features.guardian.presentation.notices.NoticesScreen
import com.escolaapp.shared.presentation.profile.*
import com.escolaapp.features.teacher.presentation.addattendance.AddAttendanceScreen
import com.escolaapp.features.teacher.presentation.grade.AddGradeScreen
import com.escolaapp.features.teacher.presentation.notice.AddNoticeScreen
import com.escolaapp.features.teacher.presentation.attendance.AttendanceCallScreen
import com.escolaapp.features.teacher.presentation.classlist.ClassListScreen
import com.escolaapp.features.teacher.presentation.gradebook.GradeBookScreen
import com.escolaapp.core.domain.model.Role
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun NavigationHandler(
    navigator: Navigator,
    events: SharedFlow<NavigationEvent>,
) {
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is NavigationEvent.ToDashboard -> {
                    when (event.role) {
                        Role.TEACHER -> navigator.replace(TeacherDashboardScreen())
                        Role.COORDINATOR -> navigator.replace(CoordinatorDashboardScreen())
                        Role.GUARDIAN -> navigator.replace(DashboardScreen())
                    }
                }

                is NavigationEvent.ToGrades -> navigator.push(
                    GradesScreen(studentId = event.studentId)
                )

                is NavigationEvent.ToAttendance -> navigator.push(
                    AttendanceScreen(studentId = event.studentId)
                )

                is NavigationEvent.ToNotices -> navigator.push(NoticesScreen())

                is NavigationEvent.ToAddGrade -> navigator.push(AddGradeScreen())

                is NavigationEvent.ToAddAttendance -> navigator.push(AddAttendanceScreen())

                is NavigationEvent.ToAddNotice -> navigator.push(AddNoticeScreen())

                is NavigationEvent.ToProfile -> navigator.push(ProfileScreen())

                is NavigationEvent.ToProfileSettings -> navigator.push(ProfileSettingsScreen())

                is NavigationEvent.ToAttendanceCall -> navigator.push(
                    AttendanceCallScreen(classId = event.classId)
                )

                is NavigationEvent.ToGradeBook -> navigator.push(
                    GradeBookScreen(classId = event.classId, bimester = event.bimester)
                )

                is NavigationEvent.ToClassList -> navigator.push(
                    ClassListScreen(teacherId = event.teacherId, mode = event.mode)
                )

                is NavigationEvent.ToLogin -> navigator.replace(LoginScreen())

                is NavigationEvent.GoBack -> navigator.pop()

                is NavigationEvent.ToTeacherDashboard -> navigator.replace(TeacherDashboardScreen())

                is NavigationEvent.GoToStudentRegistration -> navigator.push(StudentRegistrationScreen())

                NavigationEvent.GoToAddTeacher -> TODO()
                NavigationEvent.GoToClassManagement -> TODO()
                NavigationEvent.GoToCoordinatorDashboard -> TODO()
                NavigationEvent.GoToNotifications -> TODO()
                NavigationEvent.GoToSettings -> TODO()
                NavigationEvent.GoToStudentManagement -> TODO()
                NavigationEvent.GoToSubjectManagement -> TODO()
                NavigationEvent.GoToTeacherManagement -> TODO()
            }
        }
    }
}