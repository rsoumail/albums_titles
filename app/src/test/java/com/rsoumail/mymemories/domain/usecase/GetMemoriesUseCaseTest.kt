package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
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

internal class GetMemoriesUseCaseTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single { GetMemoriesUseCase(memoriesRepository = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val getMemoriesUseCase: GetMemoriesUseCase by inject()
    private lateinit var memoriesRepository: MemoriesRepository
    private val memories = listOf(Memory(1, "title", "url", "thumbnailUrl"))
    private val errorMessage = "Error"

    @BeforeEach
    fun setUp() {
        memoriesRepository = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `invoke should successfully`() {
        coEvery { memoriesRepository.getMemories() } returns flow { memories }

        runBlocking {
            val flow = getMemoriesUseCase()
            flow.collect { result ->
                Assertions.assertTrue { result is Result.Success }
                Assert.assertEquals((result as Result.Success).data, listOf(memories))
            }
            coVerify { memoriesRepository.getMemories() }
        }
    }

    @Test
    fun `invoke should failed`() {
        coEvery { memoriesRepository.getMemories() } returns flow { Result.Error(errorMessage) }

        runBlocking {
            val flow = getMemoriesUseCase()
            flow.collect { result ->
                Assertions.assertTrue { result is Result.Error }
                Assert.assertEquals((result as Result.Error).message, errorMessage)
            }
            coVerify { memoriesRepository.getMemories() }
        }
    }
}