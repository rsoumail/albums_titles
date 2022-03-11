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
            localMemoriesDataSource.getMemories().collect {
                emit(it)
            }
            if (networkDataSource.isNetworkAvailable()) {
                remoteMemoriesDataSource.getMemories().collect {
                    when (it) {
                        is Result.Success -> {
                            localMemoriesDataSource.saveMemories(it.data).collect {
                                localMemoriesDataSource.getMemories().collect { data ->
                                    emit(data)
                                }
                            }

                        }
                        else -> { emit(it) }
                    }
                }
            }
        }
    }

}