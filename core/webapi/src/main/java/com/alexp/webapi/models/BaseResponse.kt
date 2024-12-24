package com.alexp.webapi.models

data class BaseResponse<T>(
    val status: String,
    val code: Int,
    val message: String,
    val data: T,
)