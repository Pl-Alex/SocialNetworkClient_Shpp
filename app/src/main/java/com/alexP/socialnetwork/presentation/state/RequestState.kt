package com.alexP.socialnetwork.presentation.state

sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}