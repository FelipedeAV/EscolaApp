package com.escolaapp.core.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun getCurrentDate(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = "yyyy-MM-dd"
    return dateFormatter.stringFromDate(NSDate())
}