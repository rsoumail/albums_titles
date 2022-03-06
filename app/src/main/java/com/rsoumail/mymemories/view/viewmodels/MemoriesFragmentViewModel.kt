package com.rsoumail.mymemories.view.viewmodels

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.usecase.*
import com.rsoumail.mymemories.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MemoriesFragmentViewModel(
    private val isFirstLaunchUseCase: IsFirstLaunchUseCase,
    private val isNetworkAvailableUseCase: IsNetworkAvailableUseCase,
    private val updateFirstLaunchStatusUseCase: UpdateFirstLaunchStatusUseCase,
    private val getNetworkStatusNotifier: GetNetworkStatusNotifier,
    private val memoriesPagingSource: MemoriesPagingSource
) : BaseViewModel() {

    private val _showUnavailableNetwork = MutableSharedFlow<Boolean>()
    val showUnavailableNetwork: MutableSharedFlow<Boolean> = _showUnavailableNetwork

    override fun viewReady() {
        super.viewReady()
        observeNetworkStatus()
    }

    fun fetchMemories(): Flow<PagingData<Memory>> {
        return Pager(
            PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false)
        ) {
            memoriesPagingSource
        }.flow.cachedIn(viewModelScope)
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            getNetworkStatusNotifier().collect {
                _showUnavailableNetwork.emit(!it && isFirstLaunchUseCase())
            }
        }
    }

    fun updateFirstLaunch() {
        if (isFirstLaunchUseCase()) {
            updateFirstLaunchStatusUseCase()
        }
    }

    fun isUnavailableNetwork(): Boolean {
        return isFirstLaunchUseCase() && !isNetworkAvailableUseCase()
    }

}