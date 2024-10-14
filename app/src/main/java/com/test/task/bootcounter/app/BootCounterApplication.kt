package com.test.task.bootcounter.app

import android.app.Application
import com.test.task.bootcounter.di.AllModules.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class BootCounterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BootCounterApplication)
            modules(commonModules)
        }
    }
}