package app.rakha.reminder.data.local.datasource

import app.rakha.reminder.data.local.dao.EventDao
import app.rakha.reminder.data.local.entity.toEventModel
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.model.toEventEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface EventDataSource {
    fun getEvents(): Flow<List<EventModel>>

    suspend fun insertEvent(eventModel: EventModel)

    suspend fun deleteEvent(eventModel: EventModel)
}

class EventDataSourceImpl(private val eventDao: EventDao) : EventDataSource {
    override fun getEvents(): Flow<List<EventModel>> {
        return eventDao.getEvents().map { it.map { it.toEventModel() } }
    }

    override suspend fun insertEvent(eventModel: EventModel) {
        eventDao.insertEvent(eventModel.toEventEntity())
    }

    override suspend fun deleteEvent(eventModel: EventModel) {
        eventDao.deleteEvent(eventModel.uid)
    }

}