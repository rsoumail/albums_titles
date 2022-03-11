package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.framework.db.MemoryDao
import com.rsoumail.mymemories.framework.entities.MemoryModel
import io.mockk.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import kotlin.test.assertTrue

class LocalMemoriesDataSourceImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <LocalMemoriesDataSource> { LocalMemoriesDataSourceImpl(memoryDao = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val localMemoriesDataSource: LocalMemoriesDataSource by inject()
    private lateinit var memoryDao: MemoryDao
    private val memoryModel = MemoryModel(1, "title", "url", "thumbnailUrl")
    private val memory = Memory(1, "title", "url", "thumbnailUrl")
    private val errorMessage = "Get Memories error"

    @BeforeEach
    fun setUp() {
        memoryDao = declareMock<MemoryDao>()
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `it should getMemories successfully`() {
        coEvery { memoryDao.getAllMemories() } returns listOf(memoryModel)
        runBlocking {
            val flow = localMemoriesDataSource.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Success }
                Assert.assertEquals((result as Result.Success).data, listOf(memory))
            }
            coVerify { memoryDao.getAllMemories() }
        }
    }

    @Test
    fun `it should failed to getMemories`() {
        coEvery { memoryDao.getAllMemories() } returns listOf()
        runBlocking {
            val flow = localMemoriesDataSource.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Error }
                Assert.assertEquals((result as Result.Error).message, errorMessage)
            }
            coVerify { memoryDao.getAllMemories() }
        }
    }

    @Test
    fun `it should saveMemories successfully`() {
        coEvery { memoryDao.saveMemories(listOf(memoryModel)) } returns Unit
        coEvery { memoryDao.getAllMemories() } returns listOf(memoryModel)
        val entities = listOf(memory)
        runBlocking {
            val flow = localMemoriesDataSource.saveMemories(entities)
            flow.collect {
                // Request all entities
                localMemoriesDataSource.getMemories().collect {
                    assertTrue { it is Result.Success }
                    // compare result
                    Assert.assertEquals(entities, (it as Result.Success).data)
                }
            }
            coVerify { memoryDao.saveMemories(listOf(memoryModel)) }
            coVerify { memoryDao.getAllMemories() }
        }
    }
}