package app.rakha.reminder.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import app.rakha.reminder.ReminderTimeUtils
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import kotlinx.coroutines.flow.singleOrNull
import java.util.concurrent.TimeUnit

class ReminderScheduleWorker(
    private val appContext: Context,
    private val workerParams: WorkerParameters,
    private val repository: EventRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val events = repository.getEvents().singleOrNull()
        Log.d("ReminderScheduleWorker", "list of events: ${events?.size}")

        events?.forEach { event ->
            scheduleNotification(event)
        }
        return Result.success()
    }

    private fun scheduleNotification(event: EventModel) {
        val delay = ReminderTimeUtils.calculate10MinsDelayInMillis(event.eventTime)
        ReminderNotificationWorker.run(appContext, event.uid, event.title, delay)
    }

    companion object {
        const val WORKER_TAG = "ReminderScheduleWorker"

        fun run(context: Context) {
            val request = PeriodicWorkRequestBuilder<ReminderScheduleWorker>(30, TimeUnit.MINUTES)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.MINUTES)
                .addTag(WORKER_TAG)
                .build()
            val workManager = WorkManager.getInstance(context)
            workManager.enqueueUniquePeriodicWork(
                WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                request,
            )
        }
    }

}