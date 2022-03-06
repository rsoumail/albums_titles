package com.rsoumail.mymemories.framework.network

import retrofit2.Response
import retrofit2.http.GET

interface RemoteMemoriesService {

    @GET("img/shared/technical-test.json")
    suspend fun getMemories(): Response<List<Album>>
}