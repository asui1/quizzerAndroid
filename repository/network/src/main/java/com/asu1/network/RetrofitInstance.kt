package com.asu1.network

import android.net.TrafficStats
import com.asu1.models.serializers.json
import com.asu1.resources.BASE_URL_API
import com.asu1.resources.NetworkTags
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

class ContentTypeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}

@Qualifier @Retention(AnnotationRetention.BINARY)
annotation class AuthInterceptorQual

@Qualifier @Retention(AnnotationRetention.BINARY)
annotation class ContentTypeInterceptorQual

@Qualifier @Retention(AnnotationRetention.BINARY)
annotation class TrafficStatsInterceptorQual

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Interceptors
    @Provides @Singleton @AuthInterceptorQual
    fun provideAuthInterceptor(): Interceptor = BasicAuthInterceptor()

    @Provides @Singleton @ContentTypeInterceptorQual
    fun provideContentTypeInterceptor(): Interceptor = ContentTypeInterceptor()

    @Provides @Singleton @TrafficStatsInterceptorQual
    fun provideTrafficStatsInterceptor(): Interceptor = TrafficStatsInterceptor()

    // OkHttp
    @Provides @Singleton
    fun provideOkHttpClient(
        @AuthInterceptorQual auth: Interceptor,
        @ContentTypeInterceptorQual contentType: Interceptor,
        @TrafficStatsInterceptorQual traffic: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(auth)
        .addInterceptor(contentType)
        .addInterceptor(traffic)
        .build()

    // Retrofit
    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("${BASE_URL_API}quizzerServer/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    // APIs
    @Provides @Singleton
    fun provideQuizApi(retrofit: Retrofit): QuizApi =
        retrofit.create(QuizApi::class.java)

    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides @Singleton
    fun provideRecommendationApi(retrofit: Retrofit): RecommendationApi =
        retrofit.create(RecommendationApi::class.java)

    @Provides @Singleton
    fun provideNotificationApi(retrofit: Retrofit): NotificationApi =
        retrofit.create(NotificationApi::class.java)

    @Provides @Singleton
    fun provideActivityApi(retrofit: Retrofit): ActivityApi =
        retrofit.create(ActivityApi::class.java)
}
//QuizCardListDeserializer())
//UserInfoDeserializer())
//QuizCardListDeserializer())

class TrafficStatsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        TrafficStats.setThreadStatsTag(NetworkTags.QUIZZER_API)
        try {
            return chain.proceed(chain.request())
        } finally {
            TrafficStats.clearThreadStatsTag()
        }
    }
}
