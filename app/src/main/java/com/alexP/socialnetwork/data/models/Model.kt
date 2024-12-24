package com.alexP.socialnetwork.data.models

data class CreateUserResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: UserData?
)

data class EditUserResponse(
    val status: String,
    val code: Int,
    val message: String,
    val data: UserData?
)

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

data class User(
    val id: Int,
    val email: String,
    val name: String?,
    val phone: String?,
    val address: String?,
    val career: String?,
    val birthday: String?,
    val facebook: String?,
    val instagram: String?,
    val twitter: String?,
    val linkedin: String?,
    val image: String?
)

sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}