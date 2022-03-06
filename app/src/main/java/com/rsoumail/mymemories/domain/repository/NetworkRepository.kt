package com.rsoumail.mymemories.domain.repository

import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    suspend fun getNetworkStatusNotifier(): Flow<Boolean>

    fun isNetworkAvailable(): Boolean
}