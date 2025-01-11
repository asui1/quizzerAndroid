package com.asu1.quizzer

import android.app.Application
import androidx.room.Room
import com.asu1.quizzer.database.AppDatabase
import com.asu1.quizzer.musics.MusicDao
import com.asu1.quizzer.musics.MusicRepository
import com.asu1.quizzer.util.SharedPreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
class MyApplication : Application() {
    // Application-level setup
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)

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

