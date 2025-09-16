package app.rakha.reminder

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    const val TIME_FORMAT = "HH:mm"
    const val DATE_FORMAT = "MMM dd, yyyy"
    const val DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm"

    fun formatDateTime(timestamp: Long): String {
        val dateTime = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"))
    }

    fun isToday(timestamp: Long): Boolean {
        val date = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date == LocalDate.now()
    }

    fun isTomorrow(timestamp: Long): Boolean {
        val date = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date == LocalDate.now().plusDays(1)
    }

    fun getRelativeDateString(timestamp: Long): String {
        return when {
            isToday(timestamp) -> "Today"
            isTomorrow(timestamp) -> "Tomorrow"
            else -> formatDateTime(timestamp)
        }
    }

    fun formatDateTime(date: Date, format: String = DATE_TIME_FORMAT): String {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(date)
    }

}

