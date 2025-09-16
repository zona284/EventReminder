package app.rakha.reminder.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReminderFormViewModel(
    val repository: EventRepository
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

                onSuccess()
            } finally {
                _isLoading.value = false
            }
        }
    }
}