package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MemoriesRepositoryImpl(
    private val remoteMemoriesDataSource: RemoteMemoriesDataSource,
    private val localMemoriesDataSource: LocalMemoriesDataSource,
    private val networkDataSource: NetworkDataSource
) : MemoriesRepository {

    override suspend fun getMemories(): Flow<Result<List<Memory>>> {
        return flow {
            emit(localMemoriesDataSource.getMemories())
            if (networkDataSource.isNetworkAvailable()) {
                remoteMemoriesDataSource.getMemories().collect {
                    when (it) {
                        is Result.Success -> {
                            saveMemories(it.data)
                            emit(localMemoriesDataSource.getMemories())
                        }
                        else -> emit(localMemoriesDataSource.getMemories())
                    }
                }
            }
        }
    }

    override suspend fun saveMemories(memories: List<Memory>) =
        localMemoriesDataSource.saveMemories(memories)

}