package com.baubap.loginexamen.domain.repository

import com.baubap.loginexamen.data.user.User

interface UserRepository {
    suspend fun login(user: User): User
    suspend fun register(user: User): User
}