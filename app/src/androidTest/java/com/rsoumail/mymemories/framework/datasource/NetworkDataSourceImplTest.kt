package com.rsoumail.mymemories.framework.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rsoumail.mymemories.data.datasource.LocalMemoriesDataSource
import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.di.roomTestModule
import com.rsoumail.mymemories.framework.db.MemoryDao
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.extension.RegisterExtension
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock

@RunWith(AndroidJUnit4::class)
internal class NetworkDataSourceImplTest: KoinTest {

    private val networkDataSource: NetworkDataSource by inject()
    //val context = mockk<Context>()
    //val network = mockk<Network>()
    //val networkCapabilities = mockk<NetworkCapabilities>()

    @Before
    fun setUp() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(module{
                single <NetworkDataSource> { NetworkDataSourceImpl(context = get()) }
            })
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun isNetworkAvailableShouldBeTrueForWifi() {

        /*val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        every { network }
        every { cM.activeNetwork } returns network
        every { cM.getNetworkCapabilities((network)) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_WIFI) } returns true*/

        //val networkStatus = networkDataSource.isNetworkAvailable()
        //assertTrue(networkStatus)
    }

    @Test
    fun isNetworkAvailableShouldBeTrueForCellular() {
        /*val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        every { network }
        every { cM.activeNetwork } returns network
        every { cM.getNetworkCapabilities((network)) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val networkStatus = networkDataSource.isNetworkAvailable()
        assertTrue(networkStatus)*/
    }

    @Test
    fun isNetworkAvailableShouldBeFalse() {
        //val networkCapabilities = mockk<NetworkCapabilities>()
        /*val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        every { network }
        every { cM.activeNetwork } returns network
        every { cM.getNetworkCapabilities((network)) } returns null
        //every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val networkStatus = networkDataSource.isNetworkAvailable()
        assertFalse(networkStatus)*/
    }

    @Test
    fun getNetworkStatusNotifier() {
    }
}