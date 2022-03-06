package com.rsoumail.mymemories.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rsoumail.mymemories.framework.entities.MemoryModel

@Database(entities = [MemoryModel::class], version = 1, exportSchema = false)

abstract class MemoriesDatabase : RoomDatabase() {
    abstract val memoryDao: MemoryDao
}