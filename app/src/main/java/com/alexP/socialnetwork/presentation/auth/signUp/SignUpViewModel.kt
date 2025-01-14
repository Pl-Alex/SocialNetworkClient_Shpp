package com.alexP.socialnetwork.presentation.auth.signUp

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

class SignUpViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _responseState = MutableLiveData<ResponseState<AuthData>>()
    val responseState: LiveData<ResponseState<AuthData>> = _responseState

    private val _singUpFormState = MutableLiveData<SingUpFormState>().apply {
        value = SingUpFormState(email = "", password = "")
    }
    val singUpFormState: LiveData<SingUpFormState> = _singUpFormState

    fun checkData(): Boolean {
        val formState = _singUpFormState.value
        if (formState?.isDataValid == false) {
            updateEmailFormField(formState.email)
            updatePasswordFormField(formState.password)
            return false
        }
        return true
    }

    fun createUser(email: String, password: String) {
        _responseState.postValue(ResponseState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _responseState.postValue(authRepository.createUser(email, password))
        }
    }

    fun checkDataAndCreateUser() {
        if (checkData()) {
            val email = _singUpFormState.value?.email ?: return
            val password = _singUpFormState.value?.password ?: return
            createUser(email, password)
        }
    }

    fun updateEmailFormField(email: String) {
        val validationResult = validateEmail(email)
        val emailError = getValidationResultMessage(validationResult)
        _singUpFormState.value = _singUpFormState.value?.copy(
            email = email,
            emailError = emailError,
            isDataValid = emailError == null && _singUpFormState.value?.passwordError == null
        )
    }

    fun updatePasswordFormField(password: String) {
        val validationResult = validatePassword(password)
        val passwordError = getValidationResultMessage(validationResult)
        _singUpFormState.value = _singUpFormState.value?.copy(
            password = password,
            passwordError = passwordError,
            isDataValid = passwordError == null && _singUpFormState.value?.emailError == null
        )
    }

    fun resetRegistrationState() {
        _responseState.postValue(ResponseState.Initial)
    }
}