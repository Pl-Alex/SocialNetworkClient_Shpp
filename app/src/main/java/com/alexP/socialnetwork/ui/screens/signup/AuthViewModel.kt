package com.alexP.socialnetwork.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePassword
import com.alexp.textvalidation.validator.base.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataStore: DataStoreProvider,
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState get() = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            val (email, password) = dataStore.getCredentials().first()
            _authState.update {
                it.copy(
                    isAutologin = email.isNotEmpty() && password.isNotEmpty()
                )
            }
            // Is it necessary to update the state here?
            dataStore.getCredentials().collect { (email, password) ->
                _authState.update { it.copy(isAutologin = email.isNotEmpty() && password.isNotEmpty()) }
            }
        }
    }

    fun saveCredentials(email: String, password: String) {
        viewModelScope.launch {
            dataStore.saveCredentials(email, password)
        }
    }


    fun validateEmailVm(email: String): ValidationResult {
        return validateEmail(email)
    }

    fun validatePasswordVm(password: String): ValidationResult {
        return validatePassword(password)
    }

    companion object {
        fun createFactory(
            dataStore: DataStoreProvider,
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel(dataStore)
            }
        }
    }
}