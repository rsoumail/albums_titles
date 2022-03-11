package com.rsoumail.mymemories.di

import androidx.room.Room
import com.rsoumail.mymemories.framework.db.MemoriesDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

/**
 * In-Memory Room Database definition
 */
val roomTestModule = module {
    single  { // In-Memory database config
        Room.inMemoryDatabaseBuilder(androidApplication(), MemoriesDatabase::class.java)
            .allowMainThreadQueries()
            .build()}

    single { provideMemoryDao(get()) }
}