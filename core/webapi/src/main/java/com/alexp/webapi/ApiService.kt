package com.alexp.webapi

import com.alexp.webapi.models.BaseResponse
import com.alexp.webapi.models.EmailPassword
import com.alexp.webapi.models.User
import com.alexp.webapi.models.UserData
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("users")
    suspend fun createUser(
        @Field("email") email: String, @Field("password") password: String
    ): Response<BaseResponse<UserData>>

    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String,
        @Body updatedUser: User
    ): Response<BaseResponse<UserData>>

    @POST("login")
    suspend fun authorize(
        @Body emailPassword: EmailPassword
    ): Response<BaseResponse<UserData>>
}