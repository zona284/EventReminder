package app.rakha.reminder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.rakha.reminder.data.model.EventModel

@Entity("events")
data class EventEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int ? = null,
    val uid: String,
    val title: String,
    val eventTime: String,
)

fun EventEntity.toEventModel() = EventModel(
    uid = uid,
    title = title,
    eventTime = eventTime
)
