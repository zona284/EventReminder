package app.rakha.reminder.di

import androidx.room.Room
import app.rakha.reminder.data.local.AppDatabase
import app.rakha.reminder.data.local.dao.EventDao
import app.rakha.reminder.data.local.datasource.EventDataSource
import app.rakha.reminder.data.local.datasource.EventDataSourceImpl
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.data.repository.EventRepositoryImpl
import app.rakha.reminder.ui.form.ReminderFormViewModel
import app.rakha.reminder.ui.list.ReminderViewModel
import app.rakha.reminder.worker.ReminderNotificationWorker
import app.rakha.reminder.worker.ReminderScheduleWorker
import app.rakha.reminder.worker.ReminderWorkerManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object ReminderModule {
    fun getModules() = module {
        includes(database, dao, dataSource, repository, viewModel, workers)
    }

    private val database = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                AppDatabase.DB_NAME
            ).build()
        }
    }

    private val dao = module {
        single<EventDao> { get<AppDatabase>().eventDao() }
    }

    private val dataSource = module {
        single<EventDataSource> { EventDataSourceImpl(get()) }
    }

    private val repository = module {
        single<EventRepository> { EventRepositoryImpl(get()) }
    }

    private val viewModel = module {
        viewModelOf(::ReminderViewModel)
        viewModelOf(::ReminderFormViewModel)
    }

    private val workers = module {
        single { ReminderWorkerManager(get()) }
        workerOf(::ReminderScheduleWorker)
        workerOf(::ReminderNotificationWorker)
    }
}