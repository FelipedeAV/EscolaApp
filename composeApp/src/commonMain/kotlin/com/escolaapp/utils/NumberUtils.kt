package com.escolaapp.utils

import kotlin.math.abs
import kotlin.math.roundToInt

fun formatOneDecimal(value: Double): String {
    val scaled = (value * 10).roundToInt()
    val wholePart = scaled / 10
    val decimalPart = abs(scaled % 10)
    return "$wholePart.$decimalPart"
}

