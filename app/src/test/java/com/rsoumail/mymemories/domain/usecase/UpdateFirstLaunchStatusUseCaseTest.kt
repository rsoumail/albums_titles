package com.rsoumail.mymemories.domain.usecase

import com.rsoumail.mymemories.domain.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.core.component.inject
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock

internal class UpdateFirstLaunchStatusUseCaseTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single { UpdateFirstLaunchStatusUseCase(settingsRepository = get()) }
            single { IsFirstLaunchUseCase(settingsRepository = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val updateFirstLaunchStatusUseCase:UpdateFirstLaunchStatusUseCase by inject()
    private val isFirstLaunchUseCase:IsFirstLaunchUseCase by inject()
    private lateinit var settingsRepository: SettingsRepository

    @BeforeEach
    fun setUp() {
        settingsRepository = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `invoke should update first launch`() {
        every { settingsRepository.isFirstLaunch() } returns false
        every { settingsRepository.updateFirstLaunch() } returns Unit

        updateFirstLaunchStatusUseCase()

        assertFalse(isFirstLaunchUseCase())

        verify { settingsRepository.updateFirstLaunch() }
        verify { settingsRepository.isFirstLaunch() }
    }
}