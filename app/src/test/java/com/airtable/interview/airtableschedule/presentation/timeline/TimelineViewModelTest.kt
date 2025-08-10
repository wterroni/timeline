package com.airtable.interview.airtableschedule.presentation.timeline

import com.airtable.interview.airtableschedule.domain.model.Event
import com.airtable.interview.airtableschedule.domain.usecase.GetEventsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Calendar

@ExperimentalCoroutinesApi
class TimelineViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getEventsUseCase: GetEventsUseCase
    private lateinit var viewModel: TimelineViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getEventsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() = runTest {
        // Given
        val events = listOf(createTestEvent())
        every { getEventsUseCase() } returns flowOf(events)
        
        // When
        viewModel = TimelineViewModel(getEventsUseCase)
        
        // Then
        assertTrue(viewModel.uiState.value.isLoading)
        assertEquals(emptyList<Event>(), viewModel.uiState.value.events)
    }

    @Test
    fun `uiState should be updated with events when data is loaded`() = runTest {
        // Given
        val events = listOf(createTestEvent())
        every { getEventsUseCase() } returns flowOf(events)
        
        // When
        viewModel = TimelineViewModel(getEventsUseCase)

        val collectedEvents = mutableListOf<TimelineUiState>()
        val job = launch {
            viewModel.uiState.collect { state ->
                collectedEvents.add(state)
            }
        }

        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertTrue(collectedEvents.size > 1)
        assertEquals(events, collectedEvents.last().events)

        job.cancel()
    }

    private fun createTestEvent() = Event(
        id = 1,
        name = "Test Event",
        startDate = Calendar.getInstance().apply {
            set(2020, 1, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.time,
        endDate = Calendar.getInstance().apply {
            set(2020, 1, 5, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    )
}
