package com.baubap.loginexamen.domain.usecases

import com.baubap.loginexamen.data.Failure
import com.baubap.loginexamen.data.Loading
import com.baubap.loginexamen.data.Success
import com.baubap.loginexamen.data.user.User
import com.baubap.loginexamen.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(user: User) = flow {
        emit(Loading())
        val loggedUser = userRepository.login(user = user)
        emit(Success(loggedUser))
    }.catch { emit(Failure(it)) }.flowOn(Dispatchers.IO)
}