package com.alexp.textvalidation.validator

import com.alexp.textvalidation.validator.base.BaseValidator
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.textvalidation.validator.base.ValidationResult.EMPTY_FAILED
import com.alexp.textvalidation.validator.base.ValidationResult.SUCCESS

class DigitsValidator(private val input: String) : BaseValidator() {
    override fun validate(): ValidationResult {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return if (regex.matches(input))
            SUCCESS else EMPTY_FAILED
    }
}