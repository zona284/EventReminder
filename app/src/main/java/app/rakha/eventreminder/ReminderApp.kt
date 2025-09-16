package app.rakha.eventreminder

import android.app.Application
import app.rakha.reminder.di.ReminderModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ReminderApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ReminderApp)
            modules(ReminderModule.getModules())
        }
    }
}