package com.escolaapp.data.gateway

import com.escolaapp.data.models.AttendanceResponse
import com.escolaapp.data.models.GradeResponse
import com.escolaapp.data.models.LoginRequest
import com.escolaapp.data.models.LoginResponse
import com.escolaapp.data.models.NoticeResponse
import com.escolaapp.data.models.StudentResponse
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
        client.get("$baseUrl/attendance/student/$studentId") {
            header("Authorization", "Bearer $token")
        }.body()

    suspend fun getNotices(token: String): List<NoticeResponse> =
        client.get("$baseUrl/notices") {
            header("Authorization", "Bearer $token")
        }.body()
}