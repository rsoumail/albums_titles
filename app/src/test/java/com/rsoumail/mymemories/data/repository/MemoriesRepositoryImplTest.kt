package com.rsoumail.mymemories.data.repository

import app.cash.turbine.test
import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
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
            single <MemoriesRepository> { MemoriesRepositoryImpl(remoteMemoriesDataSource = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val memoriesRepository: MemoriesRepository by inject()
    private lateinit var remoteMemoriesDataSource: RemoteMemoriesDataSource
    private lateinit var networkDataSource: NetworkDataSource
    private val entitiesSave = listOf(Memory(1, "title", "url", "thumbnailUrl"))
    private val errorMessage = "Error"

    @BeforeEach
    fun setUp() {
        remoteMemoriesDataSource = declareMock {  }
        networkDataSource = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `it should getMemories successfully`() {

        coEvery { remoteMemoriesDataSource.getMemories() } returns flow {
            emit(
                Result.Success(
                    entitiesSave
                )
            )
        }

        every { networkDataSource.isNetworkAvailable() } returns true

        runBlocking {
            val flow = memoriesRepository.getMemories()
            flow.test {
                assertEquals(Result.Success(entitiesSave), awaitItem())
                awaitComplete()
            }
        }

        coVerify { remoteMemoriesDataSource.getMemories() }
    }

    @Test
    fun `it should failed to getMemories`() {

        coEvery { remoteMemoriesDataSource.getMemories() } returns flow { emit(Result.Error(errorMessage)) }

        runBlocking {
            val flow = memoriesRepository.getMemories()
            flow.test {
                assertEquals(Result.Error(errorMessage), awaitItem())
                awaitComplete()
            }
            coVerify { remoteMemoriesDataSource.getMemories() }
        }
    }
}