package com.alexP.socialnetwork.ui.auth.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexP.socialnetwork.ui.state.RequestState
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.webapi.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState


    fun createUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _requestState.postValue(RequestState.Loading)
            mainRepository.createUser(email, password)
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


    fun validateEmailVm(email: String): ValidationResult {
        return validateEmail(email)
    }

    fun validatePasswordVm(password: String): ValidationResult {
        return validatePassword(password)
    }

    fun resetRegistrationState() {
        _requestState.postValue(RequestState.Initial)
    }

    override fun onCleared() {
        super.onCleared()
        _requestState.postValue(RequestState.Initial)
    }

    companion object {
        fun createFactory(
            dataStore: DataStoreProvider,
            repository: MainRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SignUpViewModel(dataStore, repository)
            }
        }
    }
}