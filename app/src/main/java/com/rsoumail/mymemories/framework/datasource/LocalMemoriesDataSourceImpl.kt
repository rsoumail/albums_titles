package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.framework.db.MemoryDao
import com.rsoumail.mymemories.framework.entities.MemoryModel
import com.rsoumail.mymemories.utils.mapToMemory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LocalMemoriesDataSourceImpl(private val memoryDao: MemoryDao) : LocalMemoriesDataSource {

    override suspend fun getMemories(): Flow<Result<List<Memory>>> {
        return flow {
            val memories = arrayListOf<Memory>()
            val memoriesModel = memoryDao.getAllMemories()
            if (memoriesModel.isNotEmpty()) {
                memoriesModel.forEach { memoryModel -> memories.add(Memory(
                    memoryModel.id,
                    memoryModel.title,
                    memoryModel.url,
                    memoryModel.thumbnailUrl
                ) ) }
                emit(Result.Success(mapToMemory(memoriesModel)))
            } else {
                emit(Result.Error("Get Memories error"))
            }
        }
    }

    override suspend fun saveMemories(memories: List<Memory>): Flow<Unit> {
        return flow {
            val memoriesModels = arrayListOf<MemoryModel>()
            memories.forEach {
                    memory ->  memoriesModels.add(MemoryModel(memory.id, memory.title, memory.url, memory.thumbnailUrl))
            }
            memoryDao.saveMemories(memoriesModels)
            emit(Unit)
        }
    }
}