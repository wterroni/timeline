package com.airtable.interview.airtableschedule.data.source

import com.airtable.interview.airtableschedule.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.Calendar
import java.util.Date

class SampleEventDataSource : EventDataSource {
    override fun getEvents(): Flow<List<Event>> = flowOf(sampleTimelineItems)

    private val sampleTimelineItems: List<Event> = listOf(
        Event(
            id = 1,
            name = "First item",
            startDate = createDate(2020, 2, 1),
            endDate = createDate(2020, 2, 5)
        ),
        Event(
            id = 2,
            name = "Second item",
            startDate = createDate(2020, 2, 2),
            endDate = createDate(2020, 2, 8)
        ),
        Event(
            id = 3,
            name = "Another item",
            startDate = createDate(2020, 2, 6),
            endDate = createDate(2020, 2, 13)
        ),
        Event(
            id = 4,
            name = "Another item",
            startDate = createDate(2020, 2, 14),
            endDate = createDate(2020, 2, 14)
        ),
        Event(
            id = 5,
            name = "Third item",
            startDate = createDate(2020, 3, 1),
            endDate = createDate(2020, 3, 15)
        ),
        Event(
            id = 6,
            name = "Fourth item with a super long name",
            startDate = createDate(2020, 2, 12),
            endDate = createDate(2020, 3, 16)
        ),
        Event(
            id = 7,
            name = "Fifth item with a super long name",
            startDate = createDate(2020, 3, 1),
            endDate = createDate(2020, 3, 2)
        ),
        Event(
            id = 8,
            name = "First item",
            startDate = createDate(2020, 2, 3),
            endDate = createDate(2020, 2, 5)
        ),
        Event(
            id = 9,
            name = "Second item",
            startDate = createDate(2020, 2, 4),
            endDate = createDate(2020, 2, 8)
        ),
        Event(
            id = 10,
            name = "Another item",
            startDate = createDate(2020, 2, 6),
            endDate = createDate(2020, 2, 13)
        ),
        Event(
            id = 11,
            name = "Another item",
            startDate = createDate(2020, 2, 9),
            endDate = createDate(2020, 2, 9)
        ),
        Event(
            id = 12,
            name = "Third item",
            startDate = createDate(2020, 3, 1),
            endDate = createDate(2020, 3, 15)
        ),
        Event(
            id = 13,
            name = "Fourth item with a super long name",
            startDate = createDate(2020, 2, 12),
            endDate = createDate(2020, 3, 16)
        ),
        Event(
            id = 14,
            name = "Fifth item with a super long name",
            startDate = createDate(2020, 3, 1),
            endDate = createDate(2020, 3, 1)
        )
    )

    private fun createDate(year: Int, month: Int, day: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month - 1)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }
}
