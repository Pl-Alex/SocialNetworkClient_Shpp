package com.alexp.webapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ErrorResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: List<Any>? = null
)

data class UserData(
    val user: User,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class User(
    @SerialName("id") val id: Int,
    @SerialName("email") val email: String,
    @SerialName("name") val name: String?,
    @SerialName("phone") val phone: String?,
    @SerialName("address") val address: String?,
    @SerialName("career") val career: String?,
    @SerialName("birthday") val birthday: String?,
    @SerialName("facebook") val facebook: String?,
    @SerialName("instagram") val instagram: String?,
    @SerialName("twitter") val twitter: String?,
    @SerialName("linkedin") val linkedin: String?,
    @SerialName("image") val image: String?
)

sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}