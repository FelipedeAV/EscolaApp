package com.escolaapp.data.gateway

import com.escolaapp.data.models.AttendanceRequest
import com.escolaapp.data.models.AttendanceResponse
import com.escolaapp.data.models.GradeRequest
import com.escolaapp.data.models.GradeResponse
import com.escolaapp.data.models.LoginRequest
import com.escolaapp.data.models.LoginResponse
import com.escolaapp.data.models.NoticeRequest
import com.escolaapp.data.models.NoticeResponse
import com.escolaapp.data.models.StudentResponse
import com.escolaapp.data.models.UserRequest
import com.escolaapp.data.models.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiClient {

    private val baseUrl = "http://10.0.2.2:5239/api"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    suspend fun login(email: String, password: String): LoginResponse =
        client.post("$baseUrl/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }.body()

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
}