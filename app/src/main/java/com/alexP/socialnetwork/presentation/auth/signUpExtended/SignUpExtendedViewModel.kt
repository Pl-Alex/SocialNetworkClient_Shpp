package com.alexP.socialnetwork.presentation.auth.signUpExtended

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexP.socialnetwork.data.repository.AuthRepository
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.textvalidation.validatePhone
import com.alexp.textvalidation.validateUsername
import com.alexp.webapi.models.UserData
import com.alexp.webapi.models.state.ResponseState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpExtendedViewModel(
    val authRepository: AuthRepository,
) : ViewModel() {

    private val _responseState = MutableLiveData<ResponseState<UserData>>()
    val responseState: LiveData<ResponseState<UserData>> = _responseState

    private val _singUpExtendedFormState = MutableLiveData<SingUpExtendedFormState>().apply {
        value = SingUpExtendedFormState(phone = "", userName = "")
    }
    val singUpExtendedFormState: LiveData<SingUpExtendedFormState> = _singUpExtendedFormState

    fun checkData(): Boolean {
        val formState = _singUpExtendedFormState.value
        if (formState?.isDataValid == false) {
            updatePhoneFormField(formState.phone)
            updateUserNameFormField(formState.userName)
            return false
        }
        return true
    }

    fun editUser(username: String, phone: String) {
        _responseState.postValue(ResponseState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _responseState.postValue(authRepository.editUser(username, phone))
        }
    }

    fun checkDataAndEditUser() {
        if (checkData()) {
            val userName = _singUpExtendedFormState.value?.userName ?: return
            val phone = _singUpExtendedFormState.value?.phone ?: return
            editUser(userName, phone)
        }
    }

    fun updatePhoneFormField(phone: String) {
        val validationResult = validatePhone(phone)
        val phoneError = getValidationResultMessage(validationResult)
        _singUpExtendedFormState.value = _singUpExtendedFormState.value?.copy(
            phone = phone,
            phoneError = phoneError,
            isDataValid = phoneError == null && _singUpExtendedFormState.value?.userNameError == null
        )
    }

    fun updateUserNameFormField(userName: String) {
        val validationResult = validateUsername(userName)
        val userNameError = getValidationResultMessage(validationResult)
        _singUpExtendedFormState.value = _singUpExtendedFormState.value?.copy(
            userName = userName,
            userNameError = userNameError,
            isDataValid = userNameError == null && _singUpExtendedFormState.value?.phoneError == null
        )
    }

    fun resetRegistrationState() {
        _responseState.postValue(ResponseState.Initial)
    }
}