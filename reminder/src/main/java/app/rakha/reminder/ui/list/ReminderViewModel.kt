package app.rakha.reminder.ui.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.worker.ReminderNotificationWorker
import app.rakha.reminder.worker.ReminderWorkerManager
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val reminderWorkerManager: ReminderWorkerManager,
    private val repository: EventRepository
): ViewModel() {

    fun deleteEvent(event: EventModel) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            reminderWorkerManager.cancelNotificationWorker( event.uid)
            Log.d("ReminderViewModel", "Deleting event with UID: ${event.uid}")
        }
    }
    val reminderEvents = repository.getEvents()
}