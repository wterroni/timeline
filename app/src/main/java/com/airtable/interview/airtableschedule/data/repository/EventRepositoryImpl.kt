package com.airtable.interview.airtableschedule.data.repository

import com.airtable.interview.airtableschedule.data.source.EventDataSource
import com.airtable.interview.airtableschedule.domain.model.Event
import com.airtable.interview.airtableschedule.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class EventRepositoryImpl(private val eventDataSource: EventDataSource) : EventRepository {
    override fun getEvents(): Flow<List<Event>> {
        return eventDataSource.getEvents()
    }
}
