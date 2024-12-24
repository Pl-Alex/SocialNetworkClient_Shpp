package com.alexP.socialnetwork.data.repository

import android.util.Log
import com.alexP.socialnetwork.data.ApiService
import com.alexP.socialnetwork.data.models.CreateUserResponse
import com.alexP.socialnetwork.data.models.EditUserResponse
import com.alexP.socialnetwork.data.models.ErrorResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainRepository(private val apiService: ApiService) {

    suspend fun createUser(email: String, password: String): Result<CreateUserResponse> {
        val fields: HashMap<String?, RequestBody?> = HashMap()
        fields["email"] = (email).toRequestBody("text/plain".toMediaTypeOrNull())
        fields["password"] = (password).toRequestBody("text/plain".toMediaTypeOrNull())
        return try {
            val response = apiService.createUser(fields)
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

    suspend fun editUser(username: String, phone: String, userId: String, accessToken: String): Result<EditUserResponse> {
        val jsonObject = JSONObject()
        jsonObject.put("username", username)
        jsonObject.put("phone", phone)
        val jsonObjectString = jsonObject.toString()
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        return try {
            val response = apiService.editUser(userId, accessToken, requestBody)
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
}