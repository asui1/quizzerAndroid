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
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER
)
annotation class IoDispatcher

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    @Provides
    @IoDispatcher
    fun provideIo(): CoroutineDispatcher = Dispatchers.IO
}
