package app.rakha.reminder.data.repository

import app.rakha.reminder.data.local.datasource.EventDataSource
import app.rakha.reminder.data.model.EventModel
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<EventModel>>

    suspend fun insertEvent(event: EventModel)

    suspend fun deleteEvent(event: EventModel)
}

class EventRepositoryImpl(private val dataSource: EventDataSource) : EventRepository {
    override fun getEvents() = dataSource.getEvents()

    override suspend fun insertEvent(event: EventModel) = dataSource.insertEvent(event)

    override suspend fun deleteEvent(event: EventModel) = dataSource.deleteEvent(event)

}