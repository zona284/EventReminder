package app.rakha.notification

import org.koin.dsl.module

object NotificationModule {
    fun getModules() = module {
        single { NotificationHandler(get()) }
    }
}