package com.baubap.loginexamen.domain.repository.local

import com.baubap.loginexamen.data.user.InvalidLoginException
import com.baubap.loginexamen.data.user.User
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserLocalRepositoryTest {

    private val userRepository = UserLocalRepository()

    @Test(expected = InvalidLoginException::class)
    fun loginFailedTest() = runTest {
        userRepository.login(
            user = User(
                email = "achscerrillo@gmail.com",
                password = "tester123"
            )
        )
    }

    @Test
    fun loginSucceedTest() = runTest {
        val user = User(
            email = "alex@gmail.com",
            password = "Test$123"
        )
        val returnedUser = userRepository.login(
            user = user
        )
        assert(returnedUser == user)
    }
}