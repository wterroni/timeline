package com.airtable.interview.airtableschedule.data.repository

import com.airtable.interview.airtableschedule.data.source.EventDataSource
import com.airtable.interview.airtableschedule.domain.model.Event
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.Date

class EventRepositoryImplTest {

    private lateinit var eventDataSource: EventDataSource
    private lateinit var repository: EventRepositoryImpl

    @Before
    fun setup() {
        eventDataSource = mockk()
        repository = EventRepositoryImpl(eventDataSource)
    }

    @Test
    fun `getEvents should return events from data source`() = runTest {
        val testEvents = listOf(
            Event(
                id = 1,
                name = "First item",
                startDate = Calendar.getInstance().apply {
                    set(2020, 1, 1, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time,
                endDate = Calendar.getInstance().apply {
                    set(2020, 1, 5, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
            ),
            Event(
                id = 2,
                name = "Second item",
                startDate = Calendar.getInstance().apply {
                    set(2020, 1, 2, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time,
                endDate = Calendar.getInstance().apply {
                    set(2020, 1, 8, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }.time
            )
        )
        
        every { eventDataSource.getEvents() } returns flowOf(testEvents)
        
        val result = repository.getEvents().first()
        
        assertEquals(testEvents, result)
        verify(exactly = 1) { eventDataSource.getEvents() }
    }
}
