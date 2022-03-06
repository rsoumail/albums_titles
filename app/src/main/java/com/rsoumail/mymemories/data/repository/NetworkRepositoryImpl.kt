package com.rsoumail.mymemories.data.repository

import com.rsoumail.mymemories.data.datasource.NetworkDataSource
import com.rsoumail.mymemories.domain.repository.NetworkRepository

class NetworkRepositoryImpl(private val networkDataSource: NetworkDataSource): NetworkRepository {

    override suspend fun getNetworkStatusNotifier() = networkDataSource.getNetworkStatusNotifier()

    override fun isNetworkAvailable() = networkDataSource.isNetworkAvailable()
}