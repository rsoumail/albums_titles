package com.rsoumail.mymemories.framework.db

import androidx.room.*
import com.rsoumail.mymemories.framework.entities.MemoryModel

@Dao
interface MemoryDao {

    @Query("SELECT * FROM memory")
    fun getAllMemories(): List<MemoryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMemories(memories: List<MemoryModel>)
}