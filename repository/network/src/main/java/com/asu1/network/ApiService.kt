package com.asu1.network

import com.asu1.appdatamodels.Notification
import com.asu1.models.quiz.GetQuizResult
import com.asu1.models.quiz.QuizResult
import com.asu1.models.quiz.SendQuizResult
import com.asu1.models.serializers.QuizLayoutSerializer
import com.asu1.quizcardmodel.QuizCardList
import com.asu1.quizcardmodel.RecommendationList
import com.asu1.quizcardmodel.Recommendations
import com.asu1.userdatamodels.FcmToken
import com.asu1.userdatamodels.GuestAccount
import com.asu1.userdatamodels.UserActivity
import com.asu1.userdatamodels.UserInfo
import com.asu1.userdatamodels.UserRankList
import com.asu1.userdatamodels.UserRegister
import com.asu1.userdatamodels.UserRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// recommendations
interface RecommendationApi {
    @GET("recommendations")
    suspend fun getRecommendations(
        @Query("email") email: String
    ): Response<RecommendationList>

    @GET("mostviewed")  suspend fun mostViewed(): Response<Recommendations>
    @GET("mostrecent")  suspend fun mostRecent(): Response<Recommendations>
    @GET("related")     suspend fun getRelated(): Response<Recommendations>
}

// auth / account
interface AuthApi {
    @POST("guestAccount") suspend fun guestAccount(@Query("isKo") isKo: Boolean): Response<GuestAccount>
    @GET("login")         suspend fun login(@Query("email") email: String): Response<UserInfo>
    @POST("userRequest")  suspend fun userRequest(@Body request: UserRequest): Response<Void>
    @DELETE("signout")    suspend fun deleteUser(@Query("email") email: String): Response<Void>
    @GET("nicknameCheck") suspend fun checkDuplicateNickname
                (@Query("nickname") nickname: String): Response<Void>
    @POST("register")     suspend fun register(@Body body: UserRegister): Response<Void>
    @POST("fcmToken")     suspend fun updateFcmToken(@Body fcmToken: FcmToken): Response<Void>
}

// quiz
interface QuizApi {
    @GET("searchQuiz")   suspend fun searchQuiz(@Query("search") search: String): Response<QuizCardList>
    @GET("myQuiz")       suspend fun getMyQuiz(@Query("email") email: String): Response<QuizCardList>

    @DELETE("deleteQuiz")
    suspend fun deleteQuiz(@Query("uuid") uuid: String, @Query("email") email: String): Response<Void>

    @POST("addQuiz")     suspend fun addQuiz(@Body quiz: QuizLayoutSerializer): Response<Void>
    @GET("quizData")     suspend fun getQuizData(@Query("uuid") uuid: String): Response<QuizLayoutSerializer>

    @POST("submitQuiz")  suspend fun submitQuiz(@Body result: SendQuizResult): Response<QuizResult>
    @GET("result")       suspend fun getResult(@Query("resultId") id: String): Response<GetQuizResult>

    @GET("trends")       suspend fun getTrends(): Response<QuizCardList>
    @GET("ranks")        suspend fun getUserRanks(): Response<UserRankList>
}

// notifications
interface NotificationApi {
    @GET("notifications")
    suspend fun getNotifications(@Query("page") page: Int, @Query("lang") lang: String): Response<List<Notification>>

    @GET("notificationDetail")
    suspend fun getNotificationDetail(@Query("id") id: Int, @Query("lang") lang: String): Response<ResponseBody>

    @GET("onboardingNotification")
    suspend fun getOnBoardingNotification(@Query("id") id: Int): Response<Notification?>

    @GET("notificationPageNumber")
    suspend fun getNotificationPageNumber(): Response<Int>
}

// user activity
interface ActivityApi {
    @GET("userActivity")
    suspend fun getUserActivity(@Query("email") email: String): Response<List<UserActivity>>
}
