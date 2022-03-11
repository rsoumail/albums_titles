package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.NetworkRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkClass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.inject
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock

internal class GetNetworkStatusNotifierTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single { GetNetworkStatusNotifier(networkRepository = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val getNetworkStatusNotifier: GetNetworkStatusNotifier by inject()
    private lateinit var networkRepository: NetworkRepository
    private val trueEmit = true
    private val falseEmit = false

    @BeforeEach
    fun setUp() {
        networkRepository = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `invoke should emit true`() {
        coEvery { networkRepository.getNetworkStatusNotifier() } returns flow { trueEmit }

        runBlocking {
            val flow = getNetworkStatusNotifier()
            flow.collect {
                assertTrue(it)
            }

            coVerify { networkRepository.getNetworkStatusNotifier() }
        }
    }

    @Test
    fun `invoke should emit false`() {
        coEvery { networkRepository.getNetworkStatusNotifier() } returns flow { falseEmit }

        runBlocking {
            val flow = getNetworkStatusNotifier()
            flow.collect {
                assertFalse(it)
            }

            coVerify { networkRepository.getNetworkStatusNotifier() }
        }
    }
}