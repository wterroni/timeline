package com.airtable.interview.airtableschedule.domain.usecase

import com.airtable.interview.airtableschedule.domain.model.Event
import com.airtable.interview.airtableschedule.domain.repository.EventRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar

class GetEventsUseCaseTest {

    private lateinit var repository: EventRepository
    private lateinit var useCase: GetEventsUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEventsUseCase(repository)
    }

    @Test
    fun `invoke should return events from repository`() = runTest {
        // Given
        val testEvents = listOf(
            Event(
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
        )
        every { repository.getEvents() } returns flowOf(testEvents)

        // When
        val result = useCase().first()

        // Then
        assertEquals(testEvents, result)
        verify(exactly = 1) { repository.getEvents() }
    }
}
