package com.alexp.webapi

import com.alexp.webapi.models.ApiResponse
import com.alexp.webapi.models.EmailPassword
import com.alexp.webapi.models.User
import com.alexp.webapi.models.AuthData
import com.alexp.webapi.models.UserData
import retrofit2.Response
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
        @Field("email") email: String, @Field("password") password: String,
    ): Response<ApiResponse<AuthData>>

    @POST("login")
    suspend fun authorize(
        @Body emailPassword: EmailPassword
    ): Response<ApiResponse<AuthData>>

    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") userId: Int,
        @Header("Authorization") accessToken: String,
        @Body updatedUser: User,
    ): Response<ApiResponse<UserData>>
}