package com.escolaapp.core.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

actual fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}