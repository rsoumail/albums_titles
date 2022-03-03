package com.rsoumail.mymemories.utils

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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