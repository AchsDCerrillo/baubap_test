package com.baubap.loginexamen.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baubap.loginexamen.data.Failure
import com.baubap.loginexamen.data.Loading
import com.baubap.loginexamen.data.Success
import com.baubap.loginexamen.data.user.User
import com.baubap.loginexamen.domain.usecases.LoginUseCase
import com.baubap.loginexamen.ui.errors.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.baubap.loginexamen.ui.errors.PasswordValidator as PasswordValidator

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _loginState = MutableStateFlow<LoginState>(LoginIdle)
    val email: StateFlow<String> = _email.asStateFlow()
    val password: StateFlow<String> = _password.asStateFlow()
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun onEmailChange(email: String) = validator(email = email)

    fun onPasswordChange(password: String) = validator(password = password)

    fun onLoginClick() {
        val state = _loginState.value as LoginUiValid
        loginUseCase(
            user = User(
                email = state.email,
                password = state.password
            )
        ).map { outcome ->
            when(outcome) {
                is Loading -> LoginLoading
                is Success -> LoginSuccess
                is Failure -> LoginError(
                    error = outcome.error,
                    previousState = state
                )
            }
        }.onEach { newState ->
            _loginState.update { newState }
        }.launchIn(viewModelScope)
    }

    fun clearState() {
        _loginState.update {
            LoginUiValid(
                email = _email.value,
                password = _password.value
            )
        }
    }

    fun clearAll() {
        _email.update { "" }
        _password.update { "" }
        _loginState.update {
            LoginIdle
        }
    }

    private fun validator(email: String = _email.value, password: String = _password.value) {
        if (_email.value != email) _email.update { email }
        if (_password.value != password) _password.update { password }
        val passwordUiError = PasswordValidator(password = password).validate()
        val emailUiError = EmailValidator(email = email).validate()
        _loginState.update {
            when {
                passwordUiError == null && emailUiError == null -> LoginUiValid(
                    email = email,
                    password = password
                )
                else -> LoginUiInvalid(
                    emailError = emailUiError,
                    passwordError = passwordUiError
                )
            }
        }
    }
}