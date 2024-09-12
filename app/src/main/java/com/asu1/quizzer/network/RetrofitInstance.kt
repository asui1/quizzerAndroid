package com.asu1.quizzer.network

import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.QuizCardDeserializer
import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.QuizCardListDeserializer
import com.asu1.quizzer.model.UserInfoDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

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

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(contentTypeInterceptor)
        .build()
//        .addInterceptor(loggingInterceptor)

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(List::class.java, QuizCardListDeserializer())
        .registerTypeAdapter(List::class.java, UserInfoDeserializer())
        .registerTypeAdapter(object : TypeToken<List<QuizCard>>() {}.type, QuizCardListDeserializer())
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://quizzer.co.kr/api/quizzerServer/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}