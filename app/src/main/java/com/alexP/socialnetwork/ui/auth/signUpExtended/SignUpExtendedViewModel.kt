package com.alexP.socialnetwork.ui.auth.signUpExtended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexP.socialnetwork.ui.state.RequestState
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validatePhone
import com.alexp.textvalidation.validateUsername
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.webapi.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUpExtendedViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    fun editUser(username: String, phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _requestState.postValue(RequestState.Loading)
            val token = dataStore.getAccessToken().first()
            val userId = dataStore.getUserId().first()
            mainRepository.editUser(username, phone, userId, token)
                .onSuccess {
                    _requestState.postValue(RequestState.Success)
                }.onFailure { error ->
                    _requestState.postValue(RequestState.Error(error.message ?: "Unknown error"))
                }
        }
    }

    fun validateUserNameVm(userName: String): ValidationResult {
        return validateUsername(userName)
    }

    fun validatePhoneVm(phone: String): ValidationResult {
        return validatePhone(phone)
    }

    fun resetRegistrationState() {
        _requestState.postValue(RequestState.Initial)
    }

    override fun onCleared() {
        super.onCleared()
        _requestState.postValue(RequestState.Initial)
    }
}