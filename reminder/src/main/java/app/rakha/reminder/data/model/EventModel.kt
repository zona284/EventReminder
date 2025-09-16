package app.rakha.reminder.data.model

import app.rakha.reminder.data.local.entity.EventEntity

data class EventModel (
    val id: Int? = null,
    val title: String,
    val eventTime: String,
)

fun EventModel.toEventEntity() = EventEntity(
    id = id,
    title = title,
    eventTime = eventTime,
)