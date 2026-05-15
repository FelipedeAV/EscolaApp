package com.escolaapp.features.coordinator.domain.model

import kotlinx.datetime.LocalDate

data class StudentRegistrationForm(
    // Dados do aluno
    val fullName: String = "",
    val birthDate: LocalDate? = null,
    val academicEmail: String = "",
    val gender: String = "",
    val address: String = "",

    // Responsável legal
    val guardianName: String = "",
    val guardianPhone: String = "",
    val guardianEmail: String = "",

    // Informações adicionais
    val notes: String = "",
)