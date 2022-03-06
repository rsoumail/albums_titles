package com.rsoumail.mymemories.data.datasource

import kotlinx.coroutines.flow.Flow


interface NetworkDataSource{

    fun isNetworkAvailable(): Boolean

    suspend fun getNetworkStatusNotifier(): Flow<Boolean>
}