package com.escolaapp.core.data.remote.gateway

import com.escolaapp.core.data.models.AttendanceRequest
import com.escolaapp.core.data.models.AttendanceResponse
import com.escolaapp.core.data.models.AttendanceSummaryResponse
import com.escolaapp.core.data.models.BatchAttendanceRequest
import com.escolaapp.core.data.models.BatchGradeRequest
import com.escolaapp.core.data.models.ChangePasswordRequest
import com.escolaapp.core.data.models.ClassGradeSummaryResponse
import com.escolaapp.core.data.models.ClassResponse
import com.escolaapp.core.data.models.GradeRequest
import com.escolaapp.core.data.models.GradeResponse
import com.escolaapp.core.data.models.LoginRequest
import com.escolaapp.core.data.models.LoginResponse
import com.escolaapp.core.data.models.NoticeRequest
import com.escolaapp.core.data.models.NoticeResponse
import com.escolaapp.core.data.models.StudentRegistrationRequest
import com.escolaapp.core.data.models.StudentRegistrationResponse
import com.escolaapp.core.data.models.StudentResponse
import com.escolaapp.core.data.models.UserRequest
import com.escolaapp.core.data.models.UserResponse
import com.escolaapp.features.coordinator.data.model.CoordinatorClassSummaryResponse
import com.escolaapp.features.coordinator.data.model.CoordinatorDashboardResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiException(val statusCode: Int, override val message: String) : Exception(message)

class ApiClient {

    private val baseUrl = "http://10.0.2.2:5239/api"

    @OptIn(ExperimentalSerializationApi::class)
    private val jsonConfig = Json {
        namingStrategy = JsonNamingStrategy.SnakeCase
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(jsonConfig)
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    println("KTOR: $message")
                }
            }
        }
    }

    private val json = jsonConfig

    suspend fun login(email: String, password: String): LoginResponse {
        val response = client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }

        return if (response.status.value in 200..299) {
            response.body()
        } else {
            val rawBody = response.bodyAsText()
            val message = runCatching {
                json.parseToJsonElement(rawBody)
                    .jsonObject["message"]
                    ?.jsonPrimitive
                    ?.content
            }.getOrNull() ?: "Falha ao fazer login"

            throw ApiException(response.status.value, message)
        }
    }

    suspend fun getUsers(token: String): List<UserResponse> =
        client.get("$baseUrl/users") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getUserById(token: String, id: Int): UserResponse =
        client.get("$baseUrl/users/$id") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun createUser(user: UserRequest): UserResponse =
        client.post("$baseUrl/users") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()

    suspend fun changePassword(
        token: String,
        userId: Int,
        currentPassword: String,
        newPassword: String,
    ) {
        client.post("$baseUrl/users/$userId/change-password") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(
                ChangePasswordRequest(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                )
            )
        }
    }

    suspend fun getStudents(token: String): List<StudentResponse> =
        client.get("$baseUrl/students") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getStudentById(token: String, id: Int): StudentResponse =
        client.get("$baseUrl/students/$id") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getGradesByStudent(token: String, studentId: Int): List<GradeResponse> =
        client.get("$baseUrl/grades/student/$studentId") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getAttendanceByStudent(token: String, studentId: Int): List<AttendanceResponse> =
        client.get("$baseUrl/attendances/student/$studentId") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getNotices(token: String): List<NoticeResponse> =
        client.get("$baseUrl/notices") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun addGrade(token: String, request: GradeRequest): GradeResponse =
        client.post("$baseUrl/grades") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun addAttendance(token: String, request: AttendanceRequest): AttendanceResponse =
        client.post("$baseUrl/attendances") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun addNotice(token: String, request: NoticeRequest): NoticeResponse =
        client.post("$baseUrl/notices") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun getClassesByTeacher(token: String, teacherId: Int): List<ClassResponse> =
        client.get("$baseUrl/classes/teacher/$teacherId") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getCurrentClass(token: String, teacherId: Int): ClassResponse =
        client.get("$baseUrl/classes/current/$teacherId") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getAttendanceSummary(
        token: String,
        classId: Int,
        date: String
    ): AttendanceSummaryResponse =
        client.get("$baseUrl/attendances/summary/$classId?date=$date") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun sendBatchAttendance(token: String, request: BatchAttendanceRequest): String =
        client.post("$baseUrl/attendances/batch") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun getClassGradeSummary(
        token: String,
        classId: Int,
        bimester: Int
    ): ClassGradeSummaryResponse =
        client.get("$baseUrl/grades/class/$classId/bimester/$bimester") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun sendBatchGrades(token: String, request: BatchGradeRequest): String =
        client.post("$baseUrl/grades/batch") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

    suspend fun getCoordinatorDashboard(token: String): CoordinatorDashboardResponse {
        val response = client.get("$baseUrl/coordinator/dashboard") {
            header("Authorization", "Bearer $token")
        }

        val rawJson = response.bodyAsText()
        println("DEBUG COORDINATOR RAW: $rawJson")

        return json.decodeFromString(rawJson)
    }

    suspend fun getCoordinatorClasses(token: String): List<CoordinatorClassSummaryResponse> =
        client.get("$baseUrl/coordinator/classes") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun registerStudent(
        token: String,
        request: StudentRegistrationRequest,
    ): StudentRegistrationResponse =
        client.post("$baseUrl/coordinator/students") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}