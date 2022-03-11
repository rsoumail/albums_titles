package com.rsoumail.mymemories.domain.repository

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import kotlinx.coroutines.flow.Flow

interface MemoriesRepository {

    suspend fun getMemories(): Flow<Result<List<Memory>>>

}