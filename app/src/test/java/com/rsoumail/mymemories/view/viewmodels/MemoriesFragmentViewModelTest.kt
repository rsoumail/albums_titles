package com.rsoumail.mymemories.view.viewmodels

import androidx.paging.PagingSource
import com.rsoumail.mymemories.CoroutinesTestRule
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.usecase.GetNetworkStatusNotifier
import com.rsoumail.mymemories.domain.usecase.IsFirstLaunchUseCase
import com.rsoumail.mymemories.domain.usecase.IsNetworkAvailableUseCase
import com.rsoumail.mymemories.domain.usecase.UpdateFirstLaunchStatusUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.junit5.KoinTestExtension
import org.koin.test.junit5.mock.MockProviderExtension
import org.koin.test.mock.declareMock

class MemoriesFragmentViewModelTest: KoinTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @JvmField
    @RegisterExtension
    val koinTestExtension = KoinTestExtension.create {
        modules(module{
            viewModel { MemoriesFragmentViewModel(
                dispatchers = coroutinesTestRule.testDispatcherProvider,
                getNetworkStatusNotifier = get(),
                isFirstLaunchUseCase = get(),
                isNetworkAvailableUseCase = get(),
                updateFirstLaunchStatusUseCase = get(),
                memoriesPagingSource = get()) }
        })

    }

    @JvmField
    @RegisterExtension
    val mockProvider = MockProviderExtension.create { clazz ->
        mockkClass(clazz)
    }

    private val memoriesFragmentViewModel: MemoriesFragmentViewModel by inject()
    private lateinit var getNetworkStatusNotifier: GetNetworkStatusNotifier
    private lateinit var isFirstLaunchUseCase: IsFirstLaunchUseCase
    private lateinit var isNetworkAvailableUseCase: IsNetworkAvailableUseCase
    private lateinit var updateFirstLaunchStatusUseCase: UpdateFirstLaunchStatusUseCase
    private lateinit var memoriesPagingSource: MemoriesPagingSource
    private lateinit var params: PagingSource.LoadParams<Int>
    private val trueFirstLaunch = true
    private val falseFirstLaunch = false
    private val memories = listOf(Memory(1, "title", "url", "thumbnailUrl"))
    private val pagingData = PagingSource.LoadResult.Page(memories , prevKey = 0, nextKey = null)


    @BeforeEach
    fun setUp() {
        getNetworkStatusNotifier = declareMock {  }
        isFirstLaunchUseCase = declareMock {  }
        isNetworkAvailableUseCase = declareMock {  }
        updateFirstLaunchStatusUseCase = declareMock {  }
        memoriesPagingSource = declareMock {  }
        params = declareMock {  }
    }

    @AfterEach
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `updateFirstLaunch should be call`()  {
        every { isFirstLaunchUseCase() } returns trueFirstLaunch
        every { updateFirstLaunchStatusUseCase() } returns Unit


        memoriesFragmentViewModel.updateFirstLaunch()

        verify { isFirstLaunchUseCase() }
        verify { updateFirstLaunchStatusUseCase() }
    }

    @Test
    fun `updateFirstLaunch should not call`() {
        every { isFirstLaunchUseCase() } returns falseFirstLaunch

        memoriesFragmentViewModel.updateFirstLaunch()

        verify { isFirstLaunchUseCase() }
    }

    @Test
    fun `isUnavailableNetwork should return true`() {
        every { isFirstLaunchUseCase() } returns trueFirstLaunch
        every { isNetworkAvailableUseCase() } returns falseFirstLaunch

        assertTrue(memoriesFragmentViewModel.isUnavailableNetwork())

        verify { isFirstLaunchUseCase() }
        verify { isNetworkAvailableUseCase() }
    }

    @Test
    fun `isUnavailableNetwork should return false`() {
        every { isFirstLaunchUseCase() } returns trueFirstLaunch
        every { isNetworkAvailableUseCase() } returns trueFirstLaunch

        assertFalse(memoriesFragmentViewModel.isUnavailableNetwork())

        verify { isFirstLaunchUseCase() }
        verify { isNetworkAvailableUseCase() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `network status change to available`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        coEvery { getNetworkStatusNotifier() } returns flow { trueFirstLaunch }

        memoriesFragmentViewModel.viewReady()


        coVerify { getNetworkStatusNotifier() }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `network status change to unavailable`() = coroutinesTestRule.testDispatcher.runBlockingTest {

        coEvery { getNetworkStatusNotifier() } returns flow { falseFirstLaunch }

        memoriesFragmentViewModel.viewReady()


        coVerify { getNetworkStatusNotifier() }
    }

    @Test
    fun `should fetch memories`() {

        /*coEvery { memoriesPagingSource.load(params) } returns pagingData
        runBlocking {
            val flow = memoriesFragmentViewModel.fetchMemories()
            flow.collect {  }

            coVerify { memoriesPagingSource.load(params) }
        }*/

    }
}
