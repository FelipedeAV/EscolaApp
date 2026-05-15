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
                        "Teacher" -> {
                            navigator.replace(
                                TeacherDashboardScreen(
                                    token = event.token,
                                    userId = event.userId,
                                    name = event.name,
                                    email = event.email,
                                    role = event.role,
                                )
                            )
                        }
                        "Coordinator" -> {
                            navigator.replace(
                                CoordinatorDashboardScreen(
                                    token = event.token,
                                    userId = event.userId,
                                    name = event.name,
                                    email = event.email,
                                    role = event.role,
                                )
                            )
                        }
                        else -> {
                            navigator.replace(
                                DashboardScreen(
                                    token = event.token,
                                    userId = event.userId,
                                    name = event.name,
                                    email = event.email,
                                    role = event.role,
                                )
                            )
                        }
                    }
                }

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
                    ProfileScreen(
                        token  = event.token,
                        userId = event.userId,
                        name   = event.name,
                        email  = event.email,
                        role   = event.role,
                    )
                )

                is NavigationEvent.ToProfileSettings -> navigator.push(
                    ProfileSettingsScreen(
                        token = event.token,
                        userId = event.userId,
                    )
                )

                is NavigationEvent.ToAttendanceCall -> navigator.push(
                    AttendanceCallScreen(
                        token = event.token,
                        classId = event.classId,
                    )
                )

                is NavigationEvent.ToGradeBook -> navigator.push(
                    GradeBookScreen(
                        token = event.token,
                        classId = event.classId,
                    )
                )

                is NavigationEvent.ToClassList -> navigator.push(
                    ClassListScreen(
                        token = event.token,
                        teacherId = event.teacherId,
                        mode = event.mode,
                        name = event.name,
                        email = event.email,
                        role = event.role,
                    )
                )

                is NavigationEvent.ToLogin -> navigator.replace(LoginScreen())

                is NavigationEvent.GoBack -> navigator.pop()

                is NavigationEvent.ToTeacherDashboard -> navigator.replace(
                    TeacherDashboardScreen(
                        token = event.token,
                        userId = event.userId,
                        name = event.name,
                        email = event.email,
                        role = event.role,
                    )
                )

                is NavigationEvent.GoToStudentRegistration -> navigator.push(
                    StudentRegistrationScreen(token = event.token)
                )

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