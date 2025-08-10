package com.airtable.interview.airtableschedule.presentation.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airtable.interview.airtableschedule.domain.usecase.GetEventsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class TimelineViewModel(
    getEventsUseCase: GetEventsUseCase
) : ViewModel() {

    val uiState: StateFlow<TimelineUiState> = getEventsUseCase()
        .map { events -> TimelineUiState(events = events) }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TimelineUiState(isLoading = true)
        )
}
