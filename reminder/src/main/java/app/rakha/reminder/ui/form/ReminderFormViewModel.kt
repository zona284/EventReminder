package app.rakha.reminder.ui.form

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.rakha.reminder.ReminderTimeUtils
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.worker.ReminderNotificationWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderFormViewModel(
    private val context: Context,
    private val repository: EventRepository
) : ViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    private val _selectedTime = MutableStateFlow("")
    val selectedTime = _selectedTime.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateTime(time: String) {
        _selectedTime.value = time
    }

    fun saveEvent(onSuccess: () -> Unit) {
        if (_title.value.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val event = EventModel(
                    title = _title.value,
                    eventTime = _selectedTime.value
                )
                withContext(Dispatchers.IO) {
                    repository.insertEvent(event)
                }
                startScheduleNotification(event.uid, event.title, event.eventTime)
                onSuccess()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun startScheduleNotification(eventId: String, eventName: String, eventTime: String) {
        ReminderNotificationWorker.run(
            context,
            eventId,
            eventName,
            ReminderTimeUtils.calculate10MinsDelayInMillis(eventTime)
        )
    }
}