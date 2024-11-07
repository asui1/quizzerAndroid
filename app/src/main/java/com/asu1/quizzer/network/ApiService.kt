package com.asu1.quizzer.network

import com.asu1.quizzer.data.QuizLayoutSerializer
import com.asu1.quizzer.model.QuizCardList
import com.asu1.quizzer.model.Recommendations
import com.asu1.quizzer.model.UserInfo
import com.asu1.quizzer.model.UserRegister
import com.asu1.quizzer.model.UserRequest
import com.asu1.quizzer.model.VersionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("version")
    suspend fun getVersion(): Response<VersionResponse>

    @GET("recommendations")
    suspend fun getRecommendations(@Query("language") language: String): Response<Recommendations>

    @GET("login")
    suspend fun login(@Query("email") email: String): Response<UserInfo>

    @POST("userRequest")
    suspend fun userRequest(@Body request: UserRequest): Response<Void>

    @DELETE("signout")
    suspend fun deleteUser(@Query("email") email: String): Response<Void>

    @GET("nicknameCheck")
    suspend fun checkDuplicateNickname(@Query("nickname") nickname: String): Response<Void>

    @POST("register")
    suspend fun register(@Body userInfo: UserRegister): Response<Void>

    @GET("searchQuiz")
    suspend fun searchQuiz(@Query("search") search: String): Response<QuizCardList>

    @GET("myQuiz")
    suspend fun getMyQuiz(@Query("email") email: String): Response<QuizCardList>

    @DELETE("deleteQuiz")
    suspend fun deleteQuiz(@Query("uuid") email: String, @Query("email") quizId: String): Response<Void>

    @UseKotlinxSerialization
    @POST("addQuiz")
    suspend fun addQuiz(@Body quizLayoutSerializer: QuizLayoutSerializer): Response<Void>

    @UseKotlinxSerialization
    @GET("quizData")
    suspend fun getQuizData(@Query("uuid") uuid: String): Response<QuizLayoutSerializer>
}