package com.alexP.socialnetwork.presentation.auth.logIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexP.socialnetwork.data.repository.AuthRepository
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.webapi.models.AuthData
import com.alexp.webapi.models.state.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LogInViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _responseState = MutableLiveData<ResponseState<AuthData>>()
    val responseState: LiveData<ResponseState<AuthData>> = _responseState

    private val _logInFormState = MutableLiveData<LogInFormState>().apply {
        value = LogInFormState(email = "", password = "")
    }
    val logInFormState: LiveData<LogInFormState> = _logInFormState


    fun checkData(): Boolean {
        val formState = _logInFormState.value
        if (formState?.isDataValid == false) {
            updateEmailFormField(formState.email)
            updatePasswordFormField(formState.password)
            return false
        }
        return true
    }

    fun authorize(email: String, password: String) {
        _responseState.postValue(ResponseState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _responseState.postValue(authRepository.authorize(email, password))
        }
    }

    fun checkDataAndAuthorize() {
        if (checkData()) {
            val email = _logInFormState.value?.email ?: return
            val password = _logInFormState.value?.password ?: return
            authorize(email, password)
        }
    }

    fun updateEmailFormField(email: String) {
        val validationResult = validateEmail(email)
        val emailError = getValidationResultMessage(validationResult)
        _logInFormState.value = _logInFormState.value?.copy(
            email = email,
            emailError = emailError,
            isDataValid = emailError == null && _logInFormState.value?.passwordError == null
        )
    }

    fun updatePasswordFormField(password: String) {
        val validationResult = validatePassword(password)
        val passwordError = getValidationResultMessage(validationResult)
        _logInFormState.value = _logInFormState.value?.copy(
            password = password,
            passwordError = passwordError,
            isDataValid = passwordError == null && _logInFormState.value?.emailError == null
        )
    }

    fun resetRequestState() {
        _responseState.postValue(ResponseState.Initial)
    }
}