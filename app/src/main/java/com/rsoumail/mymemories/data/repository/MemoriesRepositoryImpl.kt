package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import kotlinx.coroutines.flow.Flow

class MemoriesRepositoryImpl(
    private val remoteMemoriesDataSource: RemoteMemoriesDataSource,
) : MemoriesRepository {

    override suspend fun getMemories(): Flow<Result<List<Memory>>> {
        return remoteMemoriesDataSource.getMemories()
    }

}