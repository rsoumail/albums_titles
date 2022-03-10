package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.SettingsDataSource
import com.rsoumail.mymemories.domain.repository.MemoriesRepository
import com.rsoumail.mymemories.domain.repository.SettingsRepository
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*

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

internal class SettingsRepositoryImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <SettingsRepository> { SettingsRepositoryImpl(settingsDataSource = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val settingsRepository: SettingsRepository by inject()
    private lateinit var settingsDataSource: SettingsDataSource
    private val trueFirstLaunch = true
    private val falseFirstLaunch = false

    @BeforeEach
    fun setUp() {
        settingsDataSource = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `isFirstLaunch should return true`() {
        every { settingsDataSource.isFirstLaunch() } returns trueFirstLaunch

        assertTrue(settingsRepository.isFirstLaunch())

        verify { settingsDataSource.isFirstLaunch() }
    }

    @Test
    fun `isFirstLaunch should return false`() {
        every { settingsDataSource.isFirstLaunch() } returns falseFirstLaunch

        assertFalse(settingsRepository.isFirstLaunch())

        verify { settingsDataSource.isFirstLaunch() }
    }

    @Test
    fun `should updateFirstLaunch`() {

        every { settingsDataSource.updateFirstLaunch() } returns Unit
        every { settingsDataSource.isFirstLaunch() } returns trueFirstLaunch

        settingsRepository.updateFirstLaunch()

        assertTrue(settingsRepository.isFirstLaunch())

        verify { settingsDataSource.updateFirstLaunch() }
        verify { settingsDataSource.isFirstLaunch() }

    }
}