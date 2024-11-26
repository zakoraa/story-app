package com.raflis.storyapp.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateUtil {

    fun formatToLocalizedDate(dateString: String): String {
        return try {
            val isoDateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoDateFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date = isoDateFormat.parse(dateString)

            val localizedDateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.getDefault())
            localizedDateFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid date"
        }
    }
}