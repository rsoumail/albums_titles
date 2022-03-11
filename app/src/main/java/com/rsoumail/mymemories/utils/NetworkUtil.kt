package com.rsoumail.mymemories.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.rsoumail.mymemories.domain.entities.Result
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


inline fun <reified T> createWebService(url: String, context: Context): T {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(createOkHttpClient(context))
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

fun createOkHttpClient(context: Context): OkHttpClient {
    return  OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addNetworkInterceptor(onlineInterceptor)
            .cache(Cache(context.cacheDir, (20 * 1024 * 1024).toLong()))
            .build()
}

var onlineInterceptor: Interceptor = object : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response: okhttp3.Response = chain.proceed(chain.request())
        val maxAge = 60 * 60 * 24 // read from cache for 1 day even if there is internet connection
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}


suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
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
