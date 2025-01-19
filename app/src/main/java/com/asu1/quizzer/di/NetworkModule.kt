// NetworkModule.kt
package com.asu1.quizzer.di

import com.asu1.models.serializers.json
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizzer.model.QuizCardListDeserializer
import com.asu1.quizzer.network.ApiService
import com.asu1.quizzer.network.BasicAuthInterceptor
import com.asu1.quizzer.network.ContentTypeInterceptor
import com.asu1.quizzer.network.CustomConverterFactory
import com.asu1.quizzer.network.UserInfoDeserializer
import com.asu1.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(List::class.java, QuizCardListDeserializer())
            .registerTypeAdapter(List::class.java, UserInfoDeserializer())
            .registerTypeAdapter(object : TypeToken<List<com.asu1.quizcardmodel.QuizCard>>() {}.type, QuizCardListDeserializer())
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(BasicAuthInterceptor())
            .addInterceptor(ContentTypeInterceptor())
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        val contentType = "application/json".toMediaType()
        val kotlinxSerializationConverterFactory = json.asConverterFactory(contentType)
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        return Retrofit.Builder()
            .baseUrl("${BASE_URL}quizzerServer/")
            .addConverterFactory(CustomConverterFactory(gsonConverterFactory, kotlinxSerializationConverterFactory))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}