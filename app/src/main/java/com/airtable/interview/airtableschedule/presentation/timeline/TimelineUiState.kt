package com.airtable.interview.airtableschedule.presentation.timeline

import com.airtable.interview.airtableschedule.domain.model.Event

data class TimelineUiState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
