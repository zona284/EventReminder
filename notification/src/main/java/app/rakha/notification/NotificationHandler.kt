package app.rakha.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class NotificationHandler(
    private val appContext: Context
) {
    val manager = appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showReminderNotification(eventId: String, eventName: String) {
        val channel = NotificationChannel(
            REMINDER_CHANNEL_ID,
            REMINDER_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        }
        manager.createNotificationChannel(channel)
        showNotification(eventId.hashCode(), eventName, REMINDER_CHANNEL_ID)
    }

    private fun showNotification(notifId: Int, content: String, channelId: String) {
        val notification = NotificationCompat.Builder(appContext, channelId)
            .setContentTitle("You have an event today")
            .setContentText(content)
            .setSmallIcon(R.drawable.ic_outline_alarm_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .build()

        manager.notify(notifId, notification)
    }

    companion object {
        const val REMINDER_CHANNEL_ID = "reminder_channel"
        const val REMINDER_CHANNEL_NAME = "Daily Reminders"
    }
}