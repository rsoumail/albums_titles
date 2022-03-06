package com.rsoumail.mymemories

import android.app.Application
import com.rsoumail.mymemories.di.dataModule
import com.rsoumail.mymemories.di.domainModule
import com.rsoumail.mymemories.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyMemoriesApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger(Level.ERROR)
            androidContext(this@MyMemoriesApp)
            modules(dataModule, domainModule, uiModule)
        }
    }
}