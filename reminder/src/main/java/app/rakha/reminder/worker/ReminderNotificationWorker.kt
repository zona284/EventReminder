package app.rakha.reminder.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import app.rakha.notification.NotificationHandler
import java.util.concurrent.TimeUnit

class ReminderNotificationWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
    private val notificationHandler: NotificationHandler
) : Worker(context, workerParameters) {

    override fun doWork(): Result {

        val eventId = workerParameters.inputData.getString(EVENT_ID) ?: ""
        val eventName = workerParameters.inputData.getString(EVENT_NAME) ?: ""

        notificationHandler.showReminderNotification(eventId, eventName)

        return Result.success()
    }

    companion object {
        const val WORKER_TAG = "ReminderNotificationWorker"
        const val EVENT_ID = "EVENT_ID"
        const val EVENT_NAME = "EVENT_NAME"

        fun run(context: Context, eventId: String, eventName: String, delayInMillis: Long) {
            val request =
                OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.MINUTES)
                    .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
                    .addTag(WORKER_TAG)
                    .setInputData(
                        Data.Builder()
                            .putString(EVENT_ID, eventId)
                            .putString(EVENT_NAME, eventName)
                            .build()
                    )
                    .build()
            val work = WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    "worker-$eventId",
                    ExistingWorkPolicy.REPLACE,
                    request,
                )

            Log.d("ReminderNotificationWorker", "Worker created for $eventId-$eventName: ${work.result}")
        }

        fun cancel(context: Context, eventId: String) {
            WorkManager.getInstance(context).cancelUniqueWork("worker-$eventId")
            Log.d("ReminderNotificationWorker", "Worker canceled for $eventId")
        }

    }
}