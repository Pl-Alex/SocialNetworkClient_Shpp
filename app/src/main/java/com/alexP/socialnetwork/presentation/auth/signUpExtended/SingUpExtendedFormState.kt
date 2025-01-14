package com.alexP.socialnetwork.presentation.auth.signUpExtended

data class SingUpExtendedFormState(
    val phone: String = "",
    val userName: String = "",
    val phoneError: Int? = null,
    val userNameError: Int? = null,
    val isDataValid: Boolean = false
)