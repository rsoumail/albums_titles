package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.framework.db.MemoryDao
import com.rsoumail.mymemories.framework.entities.MemoryModel
import com.rsoumail.mymemories.utils.mapToMemory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

class LocalMemoriesDataSourceImpl(private val memoryDao: MemoryDao) : LocalMemoriesDataSource {
    override suspend fun getMemories(): Result<List<Memory>> =
        suspendCancellableCoroutine { coroutine ->
                val memories = arrayListOf<Memory>()
            CoroutineScope(Dispatchers.IO).launch {
                val memoriesModel = memoryDao.getAllMemories()
                if (memoriesModel.isNotEmpty()) {
                    memoriesModel.forEach { memoryModel -> memories.add(Memory(
                        memoryModel.id,
                        memoryModel.title,
                        memoryModel.url,
                        memoryModel.thumbnailUrl
                    ) ) }
                    coroutine.resume(Result.Success(mapToMemory(memoriesModel))) {}
                } else {
                    coroutine.resume(Result.Error("Get Memories error ")) {}
                }
            }

        }

    override suspend fun saveMemories(memories: List<Memory>): Unit =
        suspendCancellableCoroutine { coroutine ->
            val memoriesModels = arrayListOf<MemoryModel>()
            memories.forEach {
                    memory ->  memoriesModels.add(MemoryModel(memory.id, memory.title, memory.url, memory.thumbnailUrl))
            }
            CoroutineScope(Dispatchers.IO).launch {
                memoryDao.saveMemories(memoriesModels)
                coroutine.resume(Unit) {}
            }
        }


}