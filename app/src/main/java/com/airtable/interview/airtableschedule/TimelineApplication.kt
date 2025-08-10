package com.airtable.interview.airtableschedule

import android.app.Application
import com.airtable.interview.airtableschedule.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TimelineApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TimelineApplication)
            modules(appModule)
        }
    }
}
