package com.alexp.webapi.di

import com.alexp.webapi.ApiService
import com.alexp.webapi.getApiService
import com.alexp.webapi.getOkHttpClient
import com.alexp.webapi.getRetrofitInstance
import com.alexp.webapi.repository.WebApiRepository
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val webApiModule = module{
     single<OkHttpClient> { getOkHttpClient() }
     single<Retrofit> { getRetrofitInstance(get<String>(named("BASE_URL")), get()) }
     single<ApiService> { getApiService(get()) }
     factory<WebApiRepository> { WebApiRepository(get()) }
}