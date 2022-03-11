package com.rsoumail.mymemories.framework.db

import androidx.room.*
import com.rsoumail.mymemories.framework.entities.MemoryModel

@Dao
interface MemoryDao {

    @Query("SELECT * FROM memory")
    suspend fun getAllMemories(): List<MemoryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMemories(memories: List<MemoryModel>)
}