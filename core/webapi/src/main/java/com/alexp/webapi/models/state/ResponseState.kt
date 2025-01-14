package com.alexp.webapi.models.state

sealed class ResponseState<out T> {
    object Initial : ResponseState<Nothing>()
    object Loading : ResponseState<Nothing>()
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Failure(val message: String) : ResponseState<Nothing>()
}