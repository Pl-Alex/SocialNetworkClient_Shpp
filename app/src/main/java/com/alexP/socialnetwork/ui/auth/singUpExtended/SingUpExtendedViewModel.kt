package com.alexP.socialnetwork.ui.auth.singUpExtended

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexP.socialnetwork.data.models.RequestState
import com.alexP.socialnetwork.data.repository.MainRepository
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.textvalidation.validatePhone
import com.alexp.textvalidation.validateUsername
import com.alexp.textvalidation.validator.base.ValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SingUpExtendedViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    private var job: Job? = null

    fun editUser(username: String, phone: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                _requestState.value = RequestState.Loading
                val token = dataStore.getAccessToken().first()
                val userId = dataStore.getUserId().first()
                mainRepository.editUser(username, phone, userId, token)
                    .onSuccess { response ->
                         _requestState.value = RequestState.Success
                    }.onFailure { error ->
                        _requestState.value =
                            RequestState.Error(error.message ?: "Unknown error")
                    }


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
        job?.cancel()
        _requestState.postValue(RequestState.Initial)
    }

    companion object {
        fun createFactory(
            dataStore: DataStoreProvider,
            repository: MainRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SingUpExtendedViewModel(dataStore, repository)
            }
        }
    }
}