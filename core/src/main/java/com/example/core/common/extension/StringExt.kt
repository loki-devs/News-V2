package com.example.core.common.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.adjustTimePattern(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'mm:ss:SSS'Z'", Locale("ind"))
    val oldPatternDate = dateFormat.parse(this)
    dateFormat.applyPattern("dd MMM yyyy")
    return dateFormat.format(oldPatternDate)
}