package com.alexP.socialnetwork.presentation.auth.signUp

data class SingUpFormState(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)