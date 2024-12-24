package com.alexP.socialnetwork.data

import com.alexP.socialnetwork.data.models.CreateUserResponse
import com.alexP.socialnetwork.data.models.EditUserResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("users")
    suspend fun createUser(
        @PartMap map: HashMap<String?, RequestBody?>
    ): Response<CreateUserResponse>

    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") userId: String,
        @Header("Authorization") accessToken: String,
        @Body requestBody: RequestBody,
    ): Response<EditUserResponse>

    companion object {
        var retrofitService: ApiService? = null
        fun getInstance(): ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://178.63.9.114:7777/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
            return retrofitService!!
        }
    }
}