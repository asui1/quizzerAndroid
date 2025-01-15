package com.asu1.quizzer

import android.app.Application
import android.os.StrictMode
import androidx.room.Room
import com.asu1.quizzer.database.AppDatabase
import com.asu1.quizzer.musics.MusicDao
import com.asu1.quizzer.musics.MusicRepository
import com.asu1.quizzer.network.RetrofitInstance
import com.asu1.quizzer.util.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication : Application() {
    // Application-level setup
    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            SharedPreferencesManager.init(this@MyApplication)
        }
        RetrofitInstance.api
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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "music-db").build()
    }

    @Provides
    fun provideMusicDao(db: AppDatabase): MusicDao{
        return db.musicDao()
    }

    @Provides
    @Singleton
    fun provideMusicRepository(musicDao: MusicDao): MusicRepository {
        return MusicRepository(musicDao)
    }
}

