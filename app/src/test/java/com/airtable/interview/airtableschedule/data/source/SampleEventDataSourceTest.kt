package com.airtable.interview.airtableschedule.data.source

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SampleEventDataSourceTest {

    private lateinit var dataSource: SampleEventDataSource

    @Before
    fun setup() {
        dataSource = SampleEventDataSource()
    }

    @Test
    fun `getEvents should return non-empty list of events`() = runTest {
        // When
        val events = dataSource.getEvents().first()

        // Then
        assertTrue(events.isNotEmpty())
    }

    @Test
    fun `getEvents should return events with valid dates`() = runTest {
        // When
        val events = dataSource.getEvents().first()

        // Then
        events.forEach { event ->
            assertNotNull(event.startDate)
            assertNotNull(event.endDate)
            assertTrue("End date should be after or equal to start date", 
                event.endDate.time >= event.startDate.time)
        }
    }

    @Test
    fun `getEvents should return events with valid IDs`() = runTest {
        // When
        val events = dataSource.getEvents().first()

        // Then
        val ids = events.map { it.id }
        assertEquals(events.size, ids.distinct().size)
        assertTrue(ids.all { it > 0 })
    }

    @Test
    fun `getEvents should return events with non-empty names`() = runTest {
        // When
        val events = dataSource.getEvents().first()

        // Then
        events.forEach { event ->
            assertNotNull(event.name)
            assertTrue(event.name.isNotEmpty())
        }
    }
}
