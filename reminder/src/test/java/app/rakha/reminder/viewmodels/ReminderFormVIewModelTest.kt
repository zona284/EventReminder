package app.rakha.reminder.viewmodels

import app.rakha.reminder.dispatchers.MainDispatcherRule
import app.rakha.reminder.data.repository.EventRepository
import app.rakha.reminder.ui.form.ReminderFormViewModel
import app.rakha.reminder.worker.ReminderWorkerManager
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReminderFormViewModelTest {
    @get:Rule
    private lateinit var mainDispatcherRule: MainDispatcherRule

    private lateinit var reminderWorkerManager: ReminderWorkerManager
    private lateinit var repository: EventRepository
    private lateinit var viewModel: ReminderFormViewModel

    @Before
    fun setup() {
        reminderWorkerManager = mockk()
        repository = mockk(relaxUnitFun = true, relaxed = true)
        viewModel = ReminderFormViewModel(reminderWorkerManager, repository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `initial state should have empty title and selectedTime, and isLoading false`() {
        // Then
        assertEquals("", viewModel.title.value)
        assertEquals("", viewModel.selectedTime.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `updateTitle should update title state`() {
        // Given
        val newTitle = "New Event Title"

        // When
        viewModel.updateTitle(newTitle)

        // Then
        assertEquals(newTitle, viewModel.title.value)
    }

    @Test
    fun `updateTime should update selectedTime state`() {
        // Given
        val newTime = "10:00"

        // When
        viewModel.updateTime(newTime)

        // Then
        assertEquals(newTime, viewModel.selectedTime.value)
    }

    @Test
    fun `saveEvent should not proceed when title is blank`() = runTest {
        // Given
        viewModel.updateTitle("")
        viewModel.updateTime("10:00")
        val onSuccess = mockk<() -> Unit>()

        // When
        viewModel.saveEvent(onSuccess)
        advanceUntilIdle()

        // Then
        verify(exactly = 0) { onSuccess() }
        coVerify(exactly = 0) { repository.insertEvent(any()) }
        verify(exactly = 0) { reminderWorkerManager.startNotificationWorker(any(), any(), any()) }
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `saveEvent should successfully save event and call onSuccess`() = runTest(UnconfinedTestDispatcher()) {
        // Given
        val eventTitle = "Test Event"
        val eventTime = "10:00"
        viewModel.updateTitle(eventTitle)
        viewModel.updateTime(eventTime)

        val onSuccess = mockk<() -> Unit>()
        every { onSuccess() } just Runs
        coEvery { repository.insertEvent(any()) } just Runs
        every { reminderWorkerManager.startNotificationWorker(any(), any(), any()) } just Runs

        // When
        viewModel.saveEvent(onSuccess)
        advanceUntilIdle()

        // Then
        coVerify {
            repository.insertEvent(any())
        }
        verify {
            reminderWorkerManager.startNotificationWorker(any(), eventTitle, eventTime)
        }
        verify{ onSuccess() }
        assertEquals(false, viewModel.isLoading.value)
    }
}