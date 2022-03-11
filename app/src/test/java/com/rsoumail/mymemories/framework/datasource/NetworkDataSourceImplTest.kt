package com.rsoumail.mymemories.framework.datasource

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
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

class NetworkDataSourceImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        //androidContext(ApplicationProvider.getApplicationContext())

        modules(module{
            single <NetworkDataSource> { NetworkDataSourceImpl(mockk<Context>()) }
        })
    }


    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val networkDataSource: NetworkDataSource by inject()
    private lateinit var context: Context
    //val context = mockk<Context>()
    private lateinit var network: Network
    private lateinit var networkCapabilities : NetworkCapabilities
    private lateinit var connectivityManager: ConnectivityManager
    private var systemService = object {}

    @BeforeEach
    fun setUp() {
        context = mockk(relaxed = true)
        network = declareMock {  }
        networkCapabilities = declareMock {  }
        connectivityManager = declareMock {  }
        systemService = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `isNetworkAvailable Should be True For Wifi`() {
        // TODO: find a way to mock service manager
        /*val connectivityManager = getApplicationContext<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCapabilities = ShadowNetworkCapabilities.newInstance()
        shadowOf(networkCapabilities).addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        shadowOf(connectivityManager).setNetworkCapabilities(connectivityManager.activeNetwork, networkCapabilities)*/

        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager as Any
        //every { systemService as ConnectivityManager } returns connectivityManager
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_WIFI) } returns true

        //val networkStatus = networkDataSource.isNetworkAvailable()
        //assertTrue(networkStatus)
    }

    @Test
    fun `isNetworkAvailable Should be True For Cellular`() {
        // TODO: find a way to mock service manager
        /*val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        every { network }
        every { cM.activeNetwork } returns network
        every { cM.getNetworkCapabilities((network)) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val networkStatus = networkDataSource.isNetworkAvailable()
        assertTrue(networkStatus)*/
    }

    @Test
    fun `isNetworkAvailable Should be False`() {
        // TODO: find a way to mock service manager
        //val networkCapabilities = mockk<NetworkCapabilities>()
        /*val cM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        every { network }
        every { cM.activeNetwork } returns network
        every { cM.getNetworkCapabilities((network)) } returns null
        //every { networkCapabilities.hasCapability(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true

        val networkStatus = networkDataSource.isNetworkAvailable()
        assertFalse(networkStatus)*/
    }

    @org.junit.Test
    fun getNetworkStatusNotifier() {
    }
}