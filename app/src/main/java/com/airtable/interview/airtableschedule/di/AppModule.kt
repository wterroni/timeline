package com.airtable.interview.airtableschedule.di

import com.airtable.interview.airtableschedule.data.repository.EventRepositoryImpl
import com.airtable.interview.airtableschedule.data.source.EventDataSource
import com.airtable.interview.airtableschedule.data.source.SampleEventDataSource
import com.airtable.interview.airtableschedule.domain.repository.EventRepository
import com.airtable.interview.airtableschedule.domain.usecase.GetEventsUseCase
import com.airtable.interview.airtableschedule.presentation.timeline.TimelineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Data Sources
    single<EventDataSource> { SampleEventDataSource() }

    // Repositories
    single<EventRepository> { EventRepositoryImpl(get()) }
    
    // Use Cases
    single { GetEventsUseCase(get()) }
    
    // ViewModels
    viewModel { TimelineViewModel(get()) }
}
