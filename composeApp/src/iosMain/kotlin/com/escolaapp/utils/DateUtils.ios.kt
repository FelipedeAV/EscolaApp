package com.escolaapp.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun getCurrentDate(): String {
    val formatter = NSDateFormatter()
    formatter.dateFormat = "yyyy-MM-dd"
    return formatter.stringFromDate(NSDate())
}

