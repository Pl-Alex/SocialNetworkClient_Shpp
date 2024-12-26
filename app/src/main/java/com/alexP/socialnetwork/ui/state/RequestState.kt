package com.alexP.socialnetwork.ui.state

sealed class RequestState {
    object Initial : RequestState()
    object Loading : RequestState()
    object Success : RequestState()
    data class Error(val message: String) : RequestState()
}