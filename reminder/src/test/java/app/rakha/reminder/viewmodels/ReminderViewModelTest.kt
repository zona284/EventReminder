package app.rakha.reminder.viewmodels

import app.rakha.reminder.dispatchers.MainDispatcherRule
import app.rakha.reminder.data.model.EventModel
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.ui.list.ReminderViewModel
import app.rakha.reminder.worker.ReminderWorkerManager
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ReminderViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ReminderViewModel

    @MockK(relaxed = true)
    private lateinit var repository: EventRepository

    @MockK
    private lateinit var reminderWorkerManager: ReminderWorkerManager

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ReminderViewModel(reminderWorkerManager, repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `reminderEvents should return events from repository`() = runTest {
        // Given
        val expectedEvents = listOf(
            EventModel(uid = "1", title = "Event 1", eventTime = "2024-01-01 09:00"),
            EventModel(uid = "2", title = "Event 2", eventTime = "2024-01-01 10:00")
        )
        every { repository.getEvents() } returns flowOf(expectedEvents)
        viewModel = ReminderViewModel(reminderWorkerManager, repository)

        // When
        val actualEvents = viewModel.reminderEvents.singleOrNull()

        // Then
        assertEquals(expectedEvents, actualEvents)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteEvent calls repository and cancels notification`() = runTest {
        val event = EventModel(uid = "123", title = "Meeting", eventTime = "09:00")

        coEvery { repository.deleteEvent(any()) } just Runs
        coEvery { reminderWorkerManager.cancelNotificationWorker(any()) } just Runs
//        every { Log.d(any(), any()) } returns 0

        viewModel.deleteEvent(event)
        advanceUntilIdle()

        coVerify { repository.deleteEvent(event) }
        coVerify { reminderWorkerManager.cancelNotificationWorker(event.uid) }
    }
}