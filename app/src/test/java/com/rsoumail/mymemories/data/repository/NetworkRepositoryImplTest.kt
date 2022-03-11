package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.domain.repository.NetworkRepository
import io.mockk.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
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

internal class NetworkRepositoryImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <NetworkRepository> { NetworkRepositoryImpl(networkDataSource = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val networkRepository: NetworkRepository by inject()
    private lateinit var networkDataSource: NetworkDataSource
    private val networkAvailable = true
    private val networkUnavailable = false

    @BeforeEach
    fun setUp() {
        networkDataSource = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `getNetworkStatusNotifier should emit true`() {
        coEvery { networkDataSource.getNetworkStatusNotifier() } returns flow { networkAvailable}

        runBlocking {
            val flow = networkRepository.getNetworkStatusNotifier()
            flow.collect {
                assertTrue(it)
            }

            coVerify { networkDataSource.getNetworkStatusNotifier() }
        }
    }

    @Test
    fun `getNetworkStatusNotifier should emit false`() {
        coEvery { networkDataSource.getNetworkStatusNotifier() } returns flow { networkUnavailable}

        runBlocking {
            val flow = networkRepository.getNetworkStatusNotifier()
            flow.collect {
                assertFalse(it)
            }

            coVerify { networkDataSource.getNetworkStatusNotifier() }
        }
    }

    @Test
    fun `isNetworkAvailable should return true`() {
        every { networkDataSource.isNetworkAvailable() } returns networkAvailable

        assertTrue(networkRepository.isNetworkAvailable())

        verify { networkDataSource.isNetworkAvailable() }
    }

    @Test
    fun `isNetworkAvailable should return false`() {
        every { networkDataSource.isNetworkAvailable() } returns networkUnavailable

        assertFalse(networkRepository.isNetworkAvailable())

        verify { networkDataSource.isNetworkAvailable() }
    }
}