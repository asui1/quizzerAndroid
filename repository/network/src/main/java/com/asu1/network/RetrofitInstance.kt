package com.asu1.network

import android.net.TrafficStats
import com.asu1.models.serializers.json
import com.asu1.quizcardmodel.QuizCardListDeserializer
import com.asu1.resources.BASE_URL_API
import com.asu1.resources.NetworkTags
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class ContentTypeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofitInstance(): RetrofitInstance {
        return RetrofitInstance
    }
}

object RetrofitInstance {
    private val authInterceptor = BasicAuthInterceptor()
    private val contentTypeInterceptor = ContentTypeInterceptor()
    private val trafficStatsInterceptor = TrafficStatsInterceptor()

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(contentTypeInterceptor)
        .addInterceptor(trafficStatsInterceptor)
        .build()

    // kotlinx.serialization JSON config
    private val json = Json {
        ignoreUnknownKeys = true       // 서버가 보내는 추가 필드 무시
        isLenient = true               // 느슨한 파싱 허용
        explicitNulls = false          // null은 생략 가능
        encodeDefaults = true
        coerceInputValues = true       // 타입 불일치 시 기본값 강제
    }

    private val contentType = "application/json".toMediaType()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("${BASE_URL_API}quizzerServer/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    // 단일 API 묶음이 아니라면 기능별로 나눠서 제공하는 것을 권장
    val quizApi: QuizApi by lazy { retrofit.create(QuizApi::class.java) }
    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val recommendationApi: RecommendationApi by lazy { retrofit.create(RecommendationApi::class.java) }
    val notificationApi: NotificationApi by lazy { retrofit.create(NotificationApi::class.java) }
    val activityApi: ActivityApi by lazy { retrofit.create(ActivityApi::class.java) }
//QuizCardListDeserializer())
//UserInfoDeserializer())
//QuizCardListDeserializer())
}

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
