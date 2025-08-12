package com.asu1.userdata

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryBindings {
    @Binds @Singleton fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds @Singleton fun bindUserSessionRepository(impl: UserSessionRepositoryImpl): UserSessionRepository
    @Binds @Singleton fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository
    @Binds @Singleton fun bindPushRepository(impl: PushRepositoryImpl): PushRepository
}
