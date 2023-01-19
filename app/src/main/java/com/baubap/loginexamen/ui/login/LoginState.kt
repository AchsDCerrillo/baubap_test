package com.baubap.loginexamen.ui.login

import com.baubap.loginexamen.ui.errors.EmptyValueError
import com.baubap.loginexamen.ui.errors.UiError

sealed interface LoginState

object LoginIdle : LoginState

object LoginLoading : LoginState

data class LoginUiInvalid(
    val emailError: UiError? = EmptyValueError(""),
    val passwordError: UiError? = EmptyValueError("")
) : LoginState

data class LoginUiValid(
    val email: String,
    val password: String
) : LoginState

data class LoginError(
    val error: Throwable? = null,
    val previousState: LoginState?
) : LoginState

object LoginSuccess : LoginState