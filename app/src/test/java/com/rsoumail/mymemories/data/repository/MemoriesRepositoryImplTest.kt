package com.rsoumail.mymemories.data.repository

import app.cash.turbine.test
import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import com.rsoumail.mymemories.framework.entities.MemoryModel
import io.mockk.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock
import kotlin.test.assertEquals

internal class MemoriesRepositoryImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <MemoriesRepository> { MemoriesRepositoryImpl(localMemoriesDataSource = get(), remoteMemoriesDataSource = get(), networkDataSource = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val memoriesRepository: MemoriesRepository by inject()
    private lateinit var localMemoriesDataSource: LocalMemoriesDataSource
    private lateinit var remoteMemoriesDataSource: RemoteMemoriesDataSource
    private lateinit var networkDataSource: NetworkDataSource
    private val entitiesGet = listOf(MemoryModel(1, "title", "url", "thumbnailUrl"))
    private val entitiesSave = listOf(Memory(1, "title", "url", "thumbnailUrl"))
    private val errorMessage = "Error"

    @BeforeEach
    fun setUp() {
        localMemoriesDataSource = declareMock {  }
        remoteMemoriesDataSource = declareMock {  }
        networkDataSource = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `it should getMemories from database`() {

        coEvery { localMemoriesDataSource.getMemories() } returns flow { entitiesGet }
        every { networkDataSource.isNetworkAvailable() } returns false

        runBlocking {
            val flow = memoriesRepository.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Success }
                Assert.assertEquals((result as Result.Success).data, listOf(entitiesGet))
            }
            coVerify { localMemoriesDataSource.getMemories() }
        }
    }

    @Test
    fun `it should getMemories from database and update from remote`() {

        coEvery { localMemoriesDataSource.getMemories() } returns flow {
            emit(
                Result.Success(
                    entitiesSave
                )
            )
        }
        coEvery { remoteMemoriesDataSource.getMemories() } returns flow {
            emit(
                Result.Success(
                    entitiesSave
                )
            )
        }
        coEvery { localMemoriesDataSource.saveMemories(Result.Success(entitiesSave).data) } returns flow { emit (Unit)}
        every { networkDataSource.isNetworkAvailable() } returns true

        runBlocking {
            val flow = memoriesRepository.getMemories()
            flow.test {
                assertEquals(Result.Success(entitiesSave), awaitItem())
                assertEquals(Result.Success(entitiesSave), awaitItem())
                awaitComplete()
            }
        }

        coVerify { localMemoriesDataSource.getMemories() }
        coVerify { remoteMemoriesDataSource.getMemories() }
        verify { networkDataSource.isNetworkAvailable() }
        coVerify { localMemoriesDataSource.saveMemories(Result.Success(entitiesSave).data) }
    }

    @Test
    fun `it should getMemories from database and failed to get from remote`() {

        coEvery { localMemoriesDataSource.getMemories() } returns flow { emit(Result.Success(entitiesSave)) }
        coEvery { remoteMemoriesDataSource.getMemories() } returns flow { emit(Result.Error(errorMessage)) }
        every { networkDataSource.isNetworkAvailable() } returns true

        runBlocking {
            val flow = memoriesRepository.getMemories()
            flow.test {
                assertEquals(Result.Success(entitiesSave), awaitItem())
                assertEquals(Result.Error(errorMessage), awaitItem())
                awaitComplete()
            }
            coVerify { localMemoriesDataSource.getMemories() }
            coVerify { remoteMemoriesDataSource.getMemories() }
            verify { networkDataSource.isNetworkAvailable() }
        }
    }
}