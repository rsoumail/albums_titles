package com.rsoumail.mymemories

import android.app.Application
import com.rsoumail.mymemories.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyMemoriesApp : Application(){
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin{
            androidLogger()
            androidContext(this@MyMemoriesApp)
            modules(appModule)
        }
    }
}