package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.SettingsDataSource
import com.rsoumail.mymemories.framework.MyMemoriesSharedPreferences
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
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

internal class SettingsDataSourceImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <SettingsDataSource> { SettingsDataSourceImpl(myMemoriesSharedPreferences = get()) }
        })
    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val settingsDataSource: SettingsDataSource by inject()
    private lateinit var myMemoriesSharedPreferences: MyMemoriesSharedPreferences
    private val trueFirstLaunch = true
    private val falseFirstLaunch = false

    @BeforeEach
    fun setUp() {
        myMemoriesSharedPreferences = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `isFirstLaunch should return true`() {
        every { myMemoriesSharedPreferences.firstLaunch } returns trueFirstLaunch

        assertTrue(settingsDataSource.isFirstLaunch())

        verify { myMemoriesSharedPreferences.firstLaunch }
    }

    @Test
    fun `isFirstLaunch should return false`() {
        every { myMemoriesSharedPreferences.firstLaunch } returns falseFirstLaunch

        assertFalse(myMemoriesSharedPreferences.firstLaunch)

        verify { myMemoriesSharedPreferences.firstLaunch }
    }

    @Test
    fun `should updateFirstLaunch`() {

        every { myMemoriesSharedPreferences.firstLaunch } returns falseFirstLaunch
        every { myMemoriesSharedPreferences.firstLaunch = falseFirstLaunch } returns Unit

        settingsDataSource.updateFirstLaunch()

        assertFalse(settingsDataSource.isFirstLaunch())

        verify { myMemoriesSharedPreferences.firstLaunch }
        verify { myMemoriesSharedPreferences.firstLaunch = falseFirstLaunch }

    }
}