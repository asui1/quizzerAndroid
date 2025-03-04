package com.asu1.network

import com.asu1.appdatamodels.Notification
import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.quizcardmodel.QuizCardList
import com.asu1.quizcardmodel.RecommendationList
import com.asu1.quizcardmodel.Recommendations
import com.asu1.userdatamodels.UserRankList
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @UseKotlinxSerialization
    @GET("recommendations")
    suspend fun getRecommendations(@Query("language") language: String, @Query("email") email: String): Response<RecommendationList>

    @UseKotlinxSerialization
    @GET("mostviewed")
    suspend fun mostViewed(@Query("language") language: String): Response<Recommendations>

    @UseKotlinxSerialization
    @GET("mostrecent")
    suspend fun mostRecent(@Query("language") language: String): Response<Recommendations>

    @UseKotlinxSerialization
    @GET("related")
    suspend fun getRelated(@Query("language") language: String): Response<Recommendations>

    @UseKotlinxSerialization
    @POST("guestAccount")
    suspend fun guestAccount(@Query("isKo") isKo: Boolean): Response<com.asu1.userdatamodels.GuestAccount>

    @GET("login")
    suspend fun login(@Query("email") email: String): Response<com.asu1.userdatamodels.UserInfo>

    @POST("userRequest")
    suspend fun userRequest(@Body request: com.asu1.userdatamodels.UserRequest): Response<Void>

    @DELETE("signout")
    suspend fun deleteUser(@Query("email") email: String): Response<Void>

    @GET("nicknameCheck")
    suspend fun checkDuplicateNickname(@Query("nickname") nickname: String): Response<Void>

    @POST("register")
    suspend fun register(@Body userInfo: com.asu1.userdatamodels.UserRegister): Response<Void>

    @UseKotlinxSerialization
    @GET("searchQuiz")
    suspend fun searchQuiz(@Query("search") search: String): Response<QuizCardList>

    @UseKotlinxSerialization
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

    @UseKotlinxSerialization
    @POST("submitQuiz")
    suspend fun submitQuiz(@Body sendQuizResult: SendQuizResult): Response<QuizResult>

    @UseKotlinxSerialization
    @GET("result")
    suspend fun getResult(@Query("resultId") resultId: String): Response<GetQuizResult>

    @UseKotlinxSerialization
    @GET("trends")
    suspend fun getTrends(@Query("language") language: String): Response<QuizCardList>

    @UseKotlinxSerialization
    @GET("ranks")
    suspend fun getUserRanks(): Response<UserRankList>

    @UseKotlinxSerialization
    @GET("notifications")
    suspend fun getNotifications(@Query("page") page: Int, @Query("lang") lang: String): Response<List<Notification>>

    @UseKotlinxSerialization
    @GET("notificationDetail")
    suspend fun getNotificationDetail(@Query("id") id: Int, @Query("lang") lang: String): Response<ResponseBody>

    @UseKotlinxSerialization
    @GET("onboardingNotification")
    suspend fun getOnBoardingNotification(@Query("id") id: Int): Response<Notification>

    @UseKotlinxSerialization
    @GET("notificationPageNumber")
    suspend fun getNotificationPageNumber(): Response<Int>

    @UseKotlinxSerialization
    @GET("userActivity")
    suspend fun getUserActivity(@Query("email") email: String): Response<List<com.asu1.userdatamodels.UserActivity>>
}