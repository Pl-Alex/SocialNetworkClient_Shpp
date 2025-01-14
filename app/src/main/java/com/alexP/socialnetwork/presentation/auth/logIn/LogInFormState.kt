package com.alexP.socialnetwork.presentation.auth.logIn

data class LogInFormState(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)