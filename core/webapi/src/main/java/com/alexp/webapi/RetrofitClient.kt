package com.alexp.webapi

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory


fun getOkHttpClient(): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

fun getRetrofitInstance(
    baseUrl: String,
    client: OkHttpClient,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
}

fun getApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}