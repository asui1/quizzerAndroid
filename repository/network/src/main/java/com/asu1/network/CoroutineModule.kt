package com.asu1.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @IoDispatcher fun provideIo(): CoroutineDispatcher = Dispatchers.IO
}
