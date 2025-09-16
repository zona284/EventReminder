package app.rakha.reminder.ui.list

import androidx.lifecycle.ViewModel
import app.rakha.reminder.data.repository.EventRepository

class ReminderViewModel(
    private val repository: EventRepository
): ViewModel() {
    val reminderEvents = repository.getEvents()

}