package com.rsoumail.mymemories.domain.repository

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import kotlinx.coroutines.flow.Flow

interface MemoriesRepository {

    //fun loadMemories(): Flow<Result<List<Memory>>>

    suspend fun getMemories(): Flow<Result<List<Memory>>>

    suspend fun saveMemories(memories: List<Memory>)

}