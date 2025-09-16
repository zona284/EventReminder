package app.rakha.reminder.ui.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.worker.ReminderNotificationWorker
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val context: Context,
    private val repository: EventRepository
): ViewModel() {
    val reminderEvents = repository.getEvents()

    fun deleteEvent(event: EventModel) {
        viewModelScope.launch {
            Log.d("ReminderViewModel", "Deleting event with UID: ${event.uid}")
            repository.deleteEvent(event)
            ReminderNotificationWorker.cancel(context, event.uid)
        }
    }
}