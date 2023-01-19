package com.baubap.loginexamen.di

import com.baubap.loginexamen.domain.repository.UserRepository
import com.baubap.loginexamen.domain.repository.local.UserLocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Provides
    @Singleton
    fun providesUserRepository(
        userLocalRepository: UserLocalRepository
    ): UserRepository = userLocalRepository
}