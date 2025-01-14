package com.alexp.textvalidation.validator.base

interface IValidator {
    fun validate(): ValidationResult
}

enum class ValidationResult {
    SUCCESS,
    EMPTY_FAILED,
    EMAIL_FAILED,
    INVALID_CHARACTERS_FAILED,
    MIN_LENGTH_FAILED,
    MAX_LENGTH_FAILED,
}