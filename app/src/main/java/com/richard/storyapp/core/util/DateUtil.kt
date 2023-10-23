package com.richard.storyapp.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateUtil {

    fun getFormattedDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)

        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val parsedDate = inputFormat.parse(date)
        return parsedDate?.let { outputFormat.format(parsedDate) } ?: ""
    }
}