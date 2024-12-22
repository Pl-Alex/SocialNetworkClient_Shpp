package com.alexP.socialnetwork.ui.main.addcontact

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.alexp.textvalidation.validateAddress
import com.alexp.textvalidation.validateCareer
import com.alexp.textvalidation.validateDateOfBirth
import com.alexp.textvalidation.validateEmail
import com.alexp.textvalidation.validatePhone
import com.alexp.textvalidation.validateUsername
import com.alexp.textvalidation.validator.base.ValidationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddContactViewModel : ViewModel() {
    private val _galleryUri = MutableStateFlow<Uri?>(null)
    val galleryUri get() = _galleryUri.asStateFlow()

    fun setGalleryUri(uri: Uri) {
        _galleryUri.value = uri
    }

    fun validateUsernameVm(userName: String): ValidationResult {
        return validateUsername(userName)
    }

    fun validateCareerVm(career: String): ValidationResult {
        return validateCareer(career)
    }

    fun validateEmailVm(email: String): ValidationResult {
        return validateEmail(email)
    }

    fun validatePhoneVm(phone: String): ValidationResult {
        return validatePhone(phone)
    }

    fun validateAddressVm(address: String): ValidationResult {
        return validateAddress(address)
    }

    fun validateDateOfBirthVm(dateOdBirth: String): ValidationResult {
        return validateDateOfBirth(dateOdBirth)
    }

}