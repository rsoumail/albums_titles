package com.rsoumail.mymemories.framework.datasource

import com.rsoumail.mymemories.data.datasource.RemoteMemoriesDataSource
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.framework.network.Album
import com.rsoumail.mymemories.framework.network.RemoteMemoriesService
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkClass
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
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
import retrofit2.Response
import java.lang.Exception
import kotlin.test.assertTrue


internal class RemoteMemoriesDataSourceImplTest: KoinTest {

    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            single <RemoteMemoriesDataSource> { RemoteMemoriesDataSourceImpl(remoteMemoriesService = get()) }
        })
    }

    private val remoteMemoriesDataSource: RemoteMemoriesDataSource by inject()
    private lateinit var remoteMemoriesService: RemoteMemoriesService
    private val successResponse = Response.success(listOf(Album(1, "title", "url", "thumbnailUrl"), Album(2, "title", "url", "thumbnailUrl")))
    private val resultEntities = listOf(Memory(1, "title", "url", "thumbnailUrl"), Memory(2, "title", "url", "thumbnailUrl"))
    private val networkError = "Response.error()"
    private val errorMessage = "Api call failed 404 $networkError"
    private val throwErrorMessage = "404 $networkError"
    private val body = "{\"errors\":[{\"message\":\"Response.error()\",\"errorType\":\"NoNetworkError\"}]}".toResponseBody()
    private lateinit var exception: Exception


    private val errorResponse = Response.error<List<Album>>(404, body )


    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    @BeforeEach
    fun setUp() {
        remoteMemoriesService = declareMock {  }
        exception = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `it should getMemories successfully`() {
        coEvery { remoteMemoriesService.getMemories() } returns successResponse
        runBlocking {
            val flow = remoteMemoriesDataSource.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Success }
                Assert.assertEquals((result as Result.Success).data, resultEntities)
            }

            coEvery { remoteMemoriesService.getMemories() }
        }
    }

    @Test
    fun `it should failed to getMemories`() {
        coEvery { remoteMemoriesService.getMemories() } returns errorResponse
        runBlocking {
            val flow = remoteMemoriesDataSource.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Error }
                Assert.assertEquals((result as Result.Error).message, errorMessage)
            }

            coEvery { remoteMemoriesService.getMemories() }
        }
    }

    @Test
    fun `it should throw error`() {
        every { exception.message } returns throwErrorMessage
        coEvery { remoteMemoriesService.getMemories() } throws  exception
        runBlocking {
            val flow = remoteMemoriesDataSource.getMemories()
            flow.collect { result ->
                assertTrue { result is Result.Error }
                Assert.assertEquals((result as Result.Error).message, errorMessage)
            }

            coEvery { remoteMemoriesService.getMemories() }
        }
    }
}