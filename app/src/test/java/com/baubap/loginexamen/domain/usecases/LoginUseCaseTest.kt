package com.baubap.loginexamen.domain.usecases

import com.baubap.loginexamen.data.Failure
import com.baubap.loginexamen.data.Success
import com.baubap.loginexamen.data.user.User
import com.baubap.loginexamen.domain.repository.local.UserLocalRepository
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LoginUseCaseTest {

    private val userRepository = UserLocalRepository()

    private val loginUseCase = LoginUseCase(
        userRepository = userRepository
    )

    @Test
    fun loginFailedTest() = runTest {
        val user = User(
            email = "achscerrillo@gmail.com",
            password = "Test$123"
        )
        val result = loginUseCase(
            user = user
        ).last()
        assert(result is Failure<User>)
    }

    @Test
    fun loginSuccessTest() = runTest {
        val user = User(
            email = "alex@gmail.com",
            password = "Test$123"
        )
        val result = loginUseCase(
            user = user
        ).last()
        assert(result is Success<User>)
    }

}