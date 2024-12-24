package com.alexp.webapi.repository

import com.alexp.webapi.ApiService
import com.alexp.webapi.models.BaseResponse
import com.alexp.webapi.models.EmailPassword
import com.alexp.webapi.models.ErrorResponse
import com.alexp.webapi.models.User
import com.alexp.webapi.models.UserData
import com.google.gson.Gson
import retrofit2.Response

class MainRepository(private val apiService: ApiService) {

    suspend fun createUser(email: String, password: String): Result<BaseResponse<UserData>> {
        return try {
            val response = apiService.createUser(email, password)
            if (response.isSuccessful) {
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

    suspend fun editUser(
        updatedUser: User,
        userId: String,
        accessToken: String
    ): Result<BaseResponse<UserData>> {
        return try {
            val response = apiService.editUser(userId, accessToken, updatedUser)
            if (response.isSuccessful) {
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

    suspend fun auth(): Response<BaseResponse<UserData>> {
        return apiService.authorize(EmailPassword("test@email", "112233"))
    }
}