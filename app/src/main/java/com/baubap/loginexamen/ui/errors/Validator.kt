package com.baubap.loginexamen.ui.errors

private fun validator(vararg errors: UiError): UiError? = errors.firstOrNull { !it.isValid() }

sealed interface UiErrorValidator {
    fun validate(): UiError?
}

data class EmailValidator(private val email: String?) : UiErrorValidator {
    override fun validate(): UiError? = validator(
        EmptyValueError(email),
        InvalidEmailError(email)
    )
}

data class PasswordValidator(private val password: String?) : UiErrorValidator {
    override fun validate(): UiError? = validator(
        EmptyValueError(password),
        MinimumSizeFieldError(password, minimumSize = 8),
        MaximumSizeFieldError(password),
        AllowedCharactersError(password)
    )
}