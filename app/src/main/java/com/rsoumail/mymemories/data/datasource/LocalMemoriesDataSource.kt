package com.rsoumail.mymemories.data.datasource

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import kotlinx.coroutines.flow.Flow

interface LocalMemoriesDataSource {

    suspend fun getMemories(): Result<List<Memory>>

    suspend fun saveMemories(memories: List<Memory>)
}