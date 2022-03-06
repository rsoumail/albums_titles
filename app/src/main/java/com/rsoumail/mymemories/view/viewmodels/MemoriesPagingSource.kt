package com.rsoumail.mymemories.view.viewmodels

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rsoumail.mymemories.domain.entities.Memory
import com.rsoumail.mymemories.domain.entities.Result
import com.rsoumail.mymemories.domain.usecase.GetMemoriesUseCase
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import java.io.IOException

class MemoriesPagingSource(
    private val getMemoriesUseCase: GetMemoriesUseCase
) : PagingSource<Int, Memory>() {
    override fun getRefreshKey(state: PagingState<Int, Memory>): Int? {
        // Our data don't support paging for now
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Memory> {
        return try {
            var memories: List<Memory>? = null
            getMemoriesUseCase().collect {
                when (it) {
                    is Result.Success -> {
                        memories = it.data
                    }
                    else -> {}
                }
            }

            LoadResult.Page(
                memories ?: listOf(),
                prevKey = null,
                nextKey = null
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}