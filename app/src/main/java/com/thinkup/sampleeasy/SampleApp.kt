package com.thinkup.sampleeasy

import android.app.Application
import com.thinkup.easypagedlist.core.PagedViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SampleApp)
            modules(module {
                factory { PagedViewModel<SampleDataSource>(get()) }
                factory { SampleDataSource(androidContext()) }
            })
        }
    }
}