package com.baubap.loginexamen.domain.repository.local

import com.baubap.loginexamen.data.user.InvalidLoginException
import com.baubap.loginexamen.data.user.User
import com.baubap.loginexamen.domain.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class UserLocalRepository @Inject constructor(

) : UserRepository {

    override suspend fun login(user: User): User {
        delay(2000)
        return when {
            user.email == "alex@gmail.com" && user.password == "Test$123" -> user
            else -> throw InvalidLoginException()
        }
    }

    override suspend fun register(user: User): User {
        TODO("Not yet implemented")
    }
}