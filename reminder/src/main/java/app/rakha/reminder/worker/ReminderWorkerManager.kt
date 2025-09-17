package app.rakha.reminder.worker

import android.content.Context
import app.rakha.reminder.ReminderTimeUtils

class ReminderWorkerManager(
    private val context: Context
) {
    fun startReminderSchedule() {
        ReminderScheduleWorker.run(context)
    }

    fun startNotificationWorker(eventId: String, eventName: String, eventTime: String) {
        ReminderNotificationWorker.run(
            context,
            eventId,
            eventName,
            ReminderTimeUtils.calculate10MinsDelayInMillis(eventTime)
        )
    }

    fun cancelNotificationWorker(eventId: String) {
        ReminderNotificationWorker.cancel(context, eventId)
    }


}