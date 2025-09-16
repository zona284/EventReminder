package app.rakha.reminder

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ReminderTimeUtils {
    const val TIME_FORMAT = "HH:mm"
    const val DATE_FORMAT = "MMM dd, yyyy"
    const val DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm"

    fun calculate10MinsDelayInMillis(eventTime: String): Long {
        val formatter = DateTimeFormatter.ofPattern(TIME_FORMAT)
        val now = LocalDateTime.now()
        val eventDateTime = LocalDateTime.of(
            now.toLocalDate(),
            LocalTime.parse(eventTime, formatter)
        )

        var delay = Duration.between(now, eventDateTime).toMillis()
        if (delay < 0) {
            // Schedule for tomorrow
            delay += Duration.ofDays(1).toMillis()
        } else {
            // check if is in range 10 mins before launch
            delay = maxOf(delay - (10 * 60 * 1000), 0)
        }
        return delay
    }

}

