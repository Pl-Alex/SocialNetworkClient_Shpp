package com.alexp.webapi.repository

import com.alexp.webapi.ApiService
import com.alexp.webapi.models.ApiErrorResponse
import com.alexp.webapi.models.AuthData
import com.alexp.webapi.models.EmailPassword
import com.alexp.webapi.models.User
import com.alexp.webapi.models.UserData
import com.alexp.webapi.models.state.ResponseState
import kotlinx.serialization.json.Json

class WebApiRepository(private val apiService: ApiService) {

    suspend fun createUser(email: String, password: String): ResponseState<AuthData> {
        return try {
            val response = apiService.createUser(email, password)
            if (response.isSuccessful) {
                ResponseState.Success(response.body()!!.data)
            } else {
                val errorResponse = response.errorBody()?.string()?.let {
                    // Api error response is not a valid json, so we need to add a closing bracket
                    Json.decodeFromString<ApiErrorResponse>(
                        it.trimEnd('}') + "}"
                    )
                }
                ResponseState.Failure(errorResponse?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            println("Exception: $e")
            ResponseState.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun authorize(email: String, password: String): ResponseState<AuthData> {
        return try {
            val response = apiService.authorize(EmailPassword(email, password))
            if (response.isSuccessful) {
                ResponseState.Success(response.body()!!.data)
            } else {
                val errorResponse = response.errorBody()?.string()
                    ?.let { Json.decodeFromString<ApiErrorResponse>(it) }
                ResponseState.Failure(errorResponse?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            println("Exception: $e")
            ResponseState.Failure(e.message ?: "Unknown error")
        }
    }

    suspend fun editUser(
        username: String,
        phone: String,
        userId: Int,
        accessToken: String,
    ): ResponseState<UserData> {
        return try {
            val response =
                apiService.editUser(userId, accessToken, User(name = username, phone = phone))
            if (response.isSuccessful) {
                ResponseState.Success(response.body()!!.data)
            } else {
                val errorResponse = response.errorBody()?.string()
                    ?.let { Json.decodeFromString<ApiErrorResponse>(it) }
                ResponseState.Failure(errorResponse?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            println("Exception: $e")
            ResponseState.Failure(e.message ?: "Unknown error")
        }
    }
}