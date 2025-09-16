package app.rakha.eventreminder

import android.app.Application
import app.rakha.notification.NotificationModule
import app.rakha.reminder.di.ReminderModule
import app.rakha.reminder.worker.ReminderScheduleWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class ReminderApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ReminderApp)
            workManagerFactory()
            modules(ReminderModule.getModules(), NotificationModule.getModules())
        }

        startReminderSchedule()
    }

    private fun startReminderSchedule() {
        ReminderScheduleWorker.run(this@ReminderApp)
    }
}