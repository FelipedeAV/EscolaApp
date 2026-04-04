package com.escolaapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun getCurrentDate(): String =
	SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())


