package com.rsoumail.mymemories.utils

import android.content.Context
import com.google.gson.GsonBuilder
import com.rsoumail.mymemories.domain.entities.Result
import okhttp3.*
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


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
            .cache(Cache(context.cacheDir, (20 * 1024 * 1024).toLong()))
            .addInterceptor(HttpLoggingInterceptor())
            .addNetworkInterceptor(onlineInterceptor)
            .addInterceptor(offlineInterceptor)
            .build()
}

val onlineInterceptor: Interceptor = object : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        val response: okhttp3.Response = chain.proceed(chain.request())
        val cacheControl = response.headers["cache-control"]
        return if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains(
                "no-cache"
            ) ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")
        ) {
            val cc = CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()
            request = request.newBuilder()
                .cacheControl(cc)
                .build()

            chain.proceed(request)

        } else {
            response
        }
    }
}


val offlineInterceptor: Interceptor = object : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {


            val cacheControl: CacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxStale(30, TimeUnit.DAYS)
                .build()

            val offlineRequest: Request = chain.request().newBuilder()
                .cacheControl(cacheControl)
                .build()
            chain.proceed(offlineRequest)
        }
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
