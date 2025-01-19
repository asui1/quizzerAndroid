package com.asu1.quizzer.network

import android.net.TrafficStats
import com.asu1.models.serializers.json
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizzer.model.QuizCardListDeserializer
import com.asu1.quizzer.util.constants.NetworkTags
import com.asu1.utils.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContentTypeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .build()
        return chain.proceed(request)
    }
}

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = BasicAuthInterceptor()
    private val contentTypeInterceptor = ContentTypeInterceptor()
    private val trafficStatsInterceptor = TrafficStatsInterceptor()

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(contentTypeInterceptor)
        .addInterceptor(trafficStatsInterceptor)
        .build()
//        .addInterceptor(loggingInterceptor)

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(List::class.java, QuizCardListDeserializer())
        .registerTypeAdapter(List::class.java, UserInfoDeserializer())
        .registerTypeAdapter(object : TypeToken<List<com.asu1.quizcardmodel.QuizCard>>() {}.type, QuizCardListDeserializer())
        .create()

    val gsonConverterFactory = GsonConverterFactory.create(gson)
    val contentType = "application/json".toMediaType()
    @OptIn(ExperimentalSerializationApi::class)
    val kotlinxSerializationConverterFactory = json.asConverterFactory(contentType)

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("${BASE_URL}quizzerServer/")
            .addConverterFactory(CustomConverterFactory(gsonConverterFactory, kotlinxSerializationConverterFactory))
            .client(client)
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
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