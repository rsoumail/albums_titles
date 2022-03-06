package com.rsoumail.mymemories.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.rsoumail.mymemories.domain.entities.Result

inline fun <reified T> createWebService(url: String): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(createOkHttpClient())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .build()
        .create(T::class.java)
}

fun createOkHttpClient(): OkHttpClient {
    return  OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>/*, mapper: (t: T?) -> S*/): Result<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return Result.Success(body)
            }
        }
        return error("${response.code()} ${response.message()}")
    } catch (e: Exception) {
        return error(e.message ?: e.toString())
    }
}

private fun <T> error(errorMessage: String): Result<T> =
    Result.Error("Api call failed $errorMessage")
