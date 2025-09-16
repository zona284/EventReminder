package app.rakha.reminder.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.rakha.reminder.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventEntity>>

    @Query("DELETE FROM events WHERE uid=:uid")
    suspend fun deleteEvent(uid: String)

}