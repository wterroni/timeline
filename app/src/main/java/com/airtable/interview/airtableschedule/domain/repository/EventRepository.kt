package com.airtable.interview.airtableschedule.domain.repository

import com.airtable.interview.airtableschedule.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvents(): Flow<List<Event>>
}
