package com.baubap.loginexamen.ui

import com.baubap.loginexamen.domain.repository.local.UserLocalRepository
import com.baubap.loginexamen.domain.usecases.LoginUseCase
import com.baubap.loginexamen.ui.errors.*
import com.baubap.loginexamen.ui.login.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private val userRepository = UserLocalRepository()

    private val loginUseCase = LoginUseCase(
        userRepository = userRepository
    )

    private val viewModel = LoginViewModel(
        loginUseCase = loginUseCase
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun emptyPasswordErrorTest() {
        viewModel.onPasswordChange("")
        val state = viewModel.loginState.value
        assert(state is LoginUiInvalid)
        assert((state as LoginUiInvalid).passwordError is EmptyValueError)
    }

    @Test
    fun minSizePasswordErrorTest() {
        viewModel.onPasswordChange("test")
        val state = viewModel.loginState.value
        assert(state is LoginUiInvalid)
        assert((state as LoginUiInvalid).passwordError is MinimumSizeFieldError)
    }

    @Test
    fun maxSizePasswordErrorTest() {
        viewModel.onPasswordChange("12345678901234567890123456789012345678901234567890123456789012345")
        val state = viewModel.loginState.value
        assert(state is LoginUiInvalid)
        assert((state as LoginUiInvalid).passwordError is MaximumSizeFieldError)
    }

    @Test
    fun invalidPasswordErrorTest() {
        viewModel.onPasswordChange("test123.45")
        val state = viewModel.loginState.value
        assert(state is LoginUiInvalid)
        assert((state as LoginUiInvalid).passwordError is AllowedCharactersError)
    }

    @Test
    fun validUserTest() {
        viewModel.onEmailChange("alex@gmail.com")
        viewModel.onPasswordChange("Test$123")
        val state = viewModel.loginState.value
        assert(state is LoginUiValid)
    }

    @Test
    fun loginErrorTest() = runBlocking {
        val deferred = async (Dispatchers.Main) {
            viewModel.onEmailChange("alexcerrillo@gmail.com")
            viewModel.onPasswordChange("Test$123")
            viewModel.onLoginClick()
            delay(2500)
            val state = viewModel.loginState.value
            assert(state is LoginError)
        }
        deferred.await()
    }

    @Test
    fun loginSuccessTest() = runBlocking {
        val deferred = async (Dispatchers.Main) {
            viewModel.onEmailChange("alex@gmail.com")
            viewModel.onPasswordChange("Test$123")
            viewModel.onLoginClick()
            delay(2500)
            val state = viewModel.loginState.value
            assert(state is LoginSuccess)
        }
        deferred.await()
    }
}