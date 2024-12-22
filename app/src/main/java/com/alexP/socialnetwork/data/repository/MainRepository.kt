package com.alexP.socialnetwork.data.repository

import com.alexP.socialnetwork.data.ApiService
import com.alexP.socialnetwork.data.models.CreateUserResponse
import com.alexP.socialnetwork.data.models.ErrorResponse
import com.google.gson.Gson

class MainRepository(private val apiService: ApiService) {

    suspend fun createUser(email: String, password: String): Result<CreateUserResponse> {

        return try {
            val response = apiService.createUser(email, password)
            if (response.isSuccessful && response.body() != null && response.body()!!.data != null) {
                Result.success(response.body()!!)
            } else {
                val errorResponse =
                    Gson().fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                Result.failure(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}