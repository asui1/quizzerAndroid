package com.asu1.quizzer

import android.app.Application
import com.asu1.appdata.suggestion.SearchSuggestionRepository
import com.asu1.network.SecurePreferences
import com.asu1.utils.LanguageSetter
import com.asu1.utils.Logger
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var suggestionRepository: SearchSuggestionRepository

    // Application-level setup
    override fun onCreate() {
        super.onCreate()

        Logger.init(BuildConfig.DEBUG)
        SecurePreferences.init(
            BuildConfig.PASSWORD,
            BuildConfig.GOOGLE_CLIENT_ID
        )
        LanguageSetter.isKo = Locale.getDefault().language == "ko"
        LanguageSetter.lang = if(Locale.getDefault().language == "ko") "ko" else "en"

//        if(BuildConfig.DEBUG){
// 문제가 왜뜨는지 까지는 되겠는데 분명 고친거같은데...
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .penaltyLog()
//                    .build()
//            )
//            StrictMode.setVmPolicy(
//                StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build()
//            )
//        }
    }
}
