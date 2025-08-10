package com.airtable.interview.airtableschedule.data.source

import com.airtable.interview.airtableschedule.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventDataSource {
    fun getEvents(): Flow<List<Event>>
}
