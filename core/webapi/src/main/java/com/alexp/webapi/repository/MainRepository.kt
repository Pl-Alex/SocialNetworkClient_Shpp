package com.alexp.webapi.repository

import com.alexp.webapi.ApiService
import com.alexp.webapi.models.ApiErrorResponse
import com.alexp.webapi.models.ApiResponse
import com.alexp.webapi.models.User
import com.alexp.webapi.models.AuthData
import com.alexp.webapi.models.EmailPassword
import com.alexp.webapi.models.UserData
import kotlinx.serialization.json.Json

class MainRepository(private val apiService: ApiService) {

    suspend fun createUser(email: String, password: String): Result<ApiResponse<AuthData>> {
        return try {
            val response = apiService.createUser(email, password)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorResponse = response.errorBody()?.string()?.let {
                    // Api error response is not a valid json, so we need to add a closing bracket
                    Json.decodeFromString<ApiErrorResponse>(
                        it.trimEnd('}') + "}"
                    )
                }
                Result.failure(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            println("Exception: $e")
            Result.failure(e)
        }
    }

    suspend fun authorize(email: String, password: String): Result<ApiResponse<AuthData>> {
        return try {
            val response = apiService.authorize(EmailPassword(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorResponse = response.errorBody()?.string()?.let {
                    // Api error response is not a valid json, so we need to add a closing bracket
                    Json.decodeFromString<ApiErrorResponse>(
                        it
                    )
                }
                Result.failure(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            println("Exception: $e")
            Result.failure(e)
        }
    }

    suspend fun editUser(
        username: String,
        phone: String,
        userId: Int,
        accessToken: String,
    ): Result<ApiResponse<UserData>> {

        return try {
            val response =
                apiService.editUser(userId, "Bearer $accessToken", User(name = username, phone = phone))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorResponse = response.errorBody()?.string()?.let {
                    println("Error response: $it")
                    // Api error response is not a valid json, so we need to add a closing bracket
                    Json.decodeFromString<ApiErrorResponse>(
                        it.trimEnd('}') + "}"
                    )
                }
                Result.failure(Exception(errorResponse?.message))
            }
        } catch (e: Exception) {
            println("Exception: $e")
            Result.failure(e)
        }
    }
}