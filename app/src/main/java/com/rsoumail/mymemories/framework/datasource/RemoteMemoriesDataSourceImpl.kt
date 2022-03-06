package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.framework.network.RemoteMemoriesService
import com.rsoumail.mymemories.utils.mapToMemory
import com.rsoumail.mymemories.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteMemoriesDataSourceImpl(private val remoteMemoriesService: RemoteMemoriesService): RemoteMemoriesDataSource {
    override suspend fun getMemories(): Flow<Result<List<Memory>>> {
        return flow {
            when (val albums = safeApiCall { remoteMemoriesService.getMemories() }) {
                is Result.Success -> emit(Result.Success(mapToMemory(albums.data)))
                is Result.Error -> emit(albums)
                else -> {}
            }
        }
    }
}