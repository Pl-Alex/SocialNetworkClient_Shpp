package com.alexP.socialnetwork.presentation.auth.logIn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexP.socialnetwork.presentation.state.RequestState
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.webapi.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LogInViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    private val _logInFormState = MutableLiveData<LogInFormState>().apply {
        value = LogInFormState(email = "", password = "")
    }
    val logInFormState: LiveData<LogInFormState> = _logInFormState

    fun authorize() {
        if (_logInFormState.value?.isDataValid == false){
            updateEmailFormField(_logInFormState.value?.email ?: "")
            updatePasswordFormField(_logInFormState.value?.password ?: "")
            return
        }

        val email = _logInFormState.value?.email ?: return
        val password = _logInFormState.value?.password ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _requestState.postValue(RequestState.Loading)
            mainRepository.authorize(email, password)
                .onSuccess { response ->
                    _requestState.postValue(RequestState.Success)
                    dataStore.setAccessToken(response.data.accessToken)
                    dataStore.setRefreshToken(response.data.refreshToken)
                    dataStore.setUserId(response.data.user.id)
                }.onFailure { error ->
                    _requestState.postValue(RequestState.Error(error.message ?: "Unknown error"))
                }
        }
    }

    fun updateEmailFormField(email: String) {
        val validationResult = validateEmail(email)
        val emailError = getValidationResultMessage(validationResult)
        _logInFormState.postValue(
            _logInFormState.value?.copy(
                email = email,
                emailError = emailError,
                isDataValid = emailError == null && _logInFormState.value?.passwordError == null
            )
        )
    }

    fun updatePasswordFormField(password: String) {
        val validationResult = validatePassword(password)
        val passwordError = getValidationResultMessage(validationResult)
        _logInFormState.postValue(
            _logInFormState.value?.copy(
                password = password,
                passwordError = passwordError,
                isDataValid = passwordError == null && _logInFormState.value?.emailError == null
            )
        )
    }

    fun resetRequestState() {
        _requestState.postValue(RequestState.Initial)
    }

    override fun onCleared() {
        super.onCleared()
        _requestState.postValue(RequestState.Initial)
    }
}