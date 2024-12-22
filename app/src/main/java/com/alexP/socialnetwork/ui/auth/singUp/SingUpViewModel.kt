package com.alexP.socialnetwork.ui.auth.singUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexP.socialnetwork.data.repository.MainRepository
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.textvalidation.validator.base.ValidationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SingUpViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: MainRepository,
) : ViewModel() {


    private val _registrationState = MutableLiveData<RegistrationState>()
    val registrationState: LiveData<RegistrationState> = _registrationState

    private var job: Job? = null


    fun createUser(email: String, password: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                _registrationState.value = RegistrationState.Loading
                mainRepository.createUser(email, password)
                    .onSuccess { response ->
                        _registrationState.value = RegistrationState.SignUpSuccess
                        dataStore.setAccessToken(response.data!!.accessToken)
                        dataStore.setRefreshToken(response.data.refreshToken)
                    }.onFailure { error ->
                        _registrationState.value =
                            RegistrationState.Error(error.message ?: "Unknown error")
                    }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        _registrationState.postValue(RegistrationState.Initial)
    }


    fun validateEmailVm(email: String): ValidationResult {
        return validateEmail(email)
    }

    fun validatePasswordVm(password: String): ValidationResult {
        return validatePassword(password)
    }

    sealed class RegistrationState {
        object Initial : RegistrationState()
        object Loading : RegistrationState()
        object SignUpSuccess : RegistrationState()
        data class Error(val message: String) : RegistrationState()
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