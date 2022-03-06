package com.rsoumail.mymemories.data.datasource

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import kotlinx.coroutines.flow.Flow

interface RemoteMemoriesDataSource {

    suspend fun getMemories(): Flow<Result<List<Memory>>>
}