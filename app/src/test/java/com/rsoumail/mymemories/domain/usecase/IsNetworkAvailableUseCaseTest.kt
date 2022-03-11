package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.NetworkRepository
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
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

internal class IsNetworkAvailableUseCaseTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single { IsNetworkAvailableUseCase(networkRepository = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val isNetworkAvailableUseCase: IsNetworkAvailableUseCase by inject()
    private lateinit var networkRepository: NetworkRepository

    @BeforeEach
    fun setUp() {
        networkRepository = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `invoke should return true`() {
        every { networkRepository.isNetworkAvailable() } returns true

        assertTrue(isNetworkAvailableUseCase())

        verify { networkRepository.isNetworkAvailable() }
    }

    @Test
    fun `invoke should return false`() {
        every { networkRepository.isNetworkAvailable() } returns false

        assertFalse(isNetworkAvailableUseCase())

        verify { networkRepository.isNetworkAvailable() }
    }
}