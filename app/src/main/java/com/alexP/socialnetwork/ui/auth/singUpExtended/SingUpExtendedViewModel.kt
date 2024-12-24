package com.alexP.socialnetwork.ui.auth.singUpExtended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexp.webapi.models.RequestState
import com.alexp.webapi.repository.MainRepository
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validatePhone
import com.alexp.textvalidation.validateUsername
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.webapi.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SingUpExtendedViewModel(
    private val dataStore: DataStoreProvider,
    private val mainRepository: com.alexp.webapi.repository.MainRepository,
) : ViewModel() {

    private val _requestState = MutableLiveData<com.alexp.webapi.models.RequestState>()
    val requestState: LiveData<com.alexp.webapi.models.RequestState> = _requestState

    private var job: Job? = null

    fun editUser(username: String, phone: String) {
        job = CoroutineScope(Dispatchers.IO).launch {
            viewModelScope.launch {
                _requestState.value = com.alexp.webapi.models.RequestState.Loading
                val token = dataStore.getAccessToken().first()
                val userId = dataStore.getUserId().first()
                val user = User(0, "", null, null, null, null, null, null, null, null, null, null)
                mainRepository.editUser(user, userId, token)
                    .onSuccess { response ->
                         _requestState.value = com.alexp.webapi.models.RequestState.Success
                    }.onFailure { error ->
                        _requestState.value =
                            com.alexp.webapi.models.RequestState.Error(error.message ?: "Unknown error")
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
        _requestState.postValue(com.alexp.webapi.models.RequestState.Initial)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        _requestState.postValue(com.alexp.webapi.models.RequestState.Initial)
    }

    companion object {
        fun createFactory(
            dataStore: DataStoreProvider,
            repository: com.alexp.webapi.repository.MainRepository,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SingUpExtendedViewModel(dataStore, repository)
            }
        }
    }
}