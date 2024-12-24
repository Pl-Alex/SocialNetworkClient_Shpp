package com.alexP.socialnetwork.ui.auth.singUp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.webapi.models.RequestState
import com.alexp.webapi.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SingUpViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<RequestState>()
    val requestState: LiveData<RequestState> = _requestState

    private var job: Job? = null

    fun createUser(email: String, password: String) {
        job = viewModelScope.launch(Dispatchers.IO) {
            _requestState.value = RequestState.Loading
            mainRepository.createUser(email, password)
                .onSuccess { response ->
                    _requestState.value = RequestState.Success
                    dataStore.setAccessToken(response.data!!.accessToken)
                    dataStore.setRefreshToken(response.data.refreshToken)
                    dataStore.setUserId(response.data.user.id.toString())
                }.onFailure { error ->
                    _requestState.value =
                        RequestState.Error(error.message ?: "Unknown error")
                }
        }

    }

    fun testAuth() {
        viewModelScope.launch {
            val authResult = mainRepository.auth()
            Log.d("AAA", authResult.body().toString())
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
        job?.cancel()
        _requestState.postValue(RequestState.Initial)
    }

    companion object {
        fun createFactory(
            dataStore: DataStoreProvider,
            repository: MainRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SingUpViewModel(dataStore, repository)
            }
        }
    }
}