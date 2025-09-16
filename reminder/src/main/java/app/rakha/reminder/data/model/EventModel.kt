package app.rakha.reminder.data.model

import app.rakha.reminder.data.local.entity.EventEntity
import java.util.UUID

data class EventModel (
    val uid: String = UUID.randomUUID().toString(),
    val title: String,
    val eventTime: String,
)

fun EventModel.toEventEntity() = EventEntity(
    uid = uid,
    title = title,
    eventTime = eventTime,
)