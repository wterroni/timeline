package com.airtable.interview.airtableschedule.domain.usecase

import com.airtable.interview.airtableschedule.domain.model.Event
import com.airtable.interview.airtableschedule.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow

class GetEventsUseCase(private val eventRepository: EventRepository) {
    operator fun invoke(): Flow<List<Event>> {
        return eventRepository.getEvents()
    }
}
