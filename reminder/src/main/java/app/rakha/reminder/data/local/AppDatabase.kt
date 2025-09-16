package app.rakha.reminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.rakha.reminder.data.local.dao.EventDao
import app.rakha.reminder.data.local.entity.EventEntity

@Database(
    entities = [EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {
        const val DB_NAME = "app_database"
    }
}