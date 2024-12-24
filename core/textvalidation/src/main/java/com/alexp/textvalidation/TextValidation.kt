package com.alexp.textvalidation

import com.alexp.textvalidation.validator.DigitsValidator
import com.alexp.textvalidation.validator.base.BaseValidator
import com.alexp.textvalidation.validator.EmailValidator
import com.alexp.textvalidation.validator.EmptyValidator
import com.alexp.textvalidation.validator.PasswordValidator
import com.alexp.textvalidation.validator.base.ValidationResult


fun validateEmail(email: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(email), EmailValidator(email))
}

fun validatePassword(password: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(password), PasswordValidator(password))
}

fun validateUsername(username: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(username))
}

fun validatePhone(phone: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(phone), DigitsValidator(phone))
}

fun validateCareer(career: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(career))
}

fun validateAddress(address: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(address))
}

fun validateDateOfBirth(dateOfBirth: String): ValidationResult {
    return BaseValidator.validate(EmptyValidator(dateOfBirth))
}