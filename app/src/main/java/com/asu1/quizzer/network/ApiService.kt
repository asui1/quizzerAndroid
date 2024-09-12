package com.asu1.quizzer.network

import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.Recommendations
import com.asu1.quizzer.model.UserInfo
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.model.VersionResponse
import com.google.gson.annotations.SerializedName
import org.intellij.lang.annotations.Language
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @GET("version")
    suspend fun getVersion(): Response<VersionResponse>

    @GET("GetRecommendations2")
    suspend fun getRecommendations(@Query("language") language: String): Response<Recommendations>

    @GET("login2")
    suspend fun login(@Query("email") email: String): Response<UserInfo>

    @POST("UserRequest")
    suspend fun userRequest(@Body request: UserRequest): Response<Void>

    @DELETE("DeleteUser")
    suspend fun deleteUser(@Query("email") email: String): Response<Void>

    @GET("CheckDuplicateNickname")
    suspend fun checkDuplicateNickname(@Query("nickname") nickname: String): Response<Void>

    @POST("createUser")
    suspend fun register(@Body userInfo: UserRegister): Response<Void>

    @GET("searchQuiz2")
    suspend fun searchQuiz(@Query("search") search: String): Response<QuizCardList>
}