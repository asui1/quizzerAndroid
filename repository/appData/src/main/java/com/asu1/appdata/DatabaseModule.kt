package com.asu1.appdata

import android.content.Context
import com.asu1.appdata.music.MusicDao
import com.asu1.appdata.stringFilter.AdminWordDao
import com.asu1.appdata.stringFilter.InappropriateWordDao
import com.asu1.appdata.suggestion.SearchSuggestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
//        return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
//            .addMigrations(AppDatabase.MIGRATION_1_2)
//            .createFromAsset(PREPOPULATE_DB)
//            .fallbackToDestructiveMigration()
//            .build()
    }

    @Provides
    fun provideMusicDao(database: AppDatabase): MusicDao = database.musicDao()

    @Provides
    fun provideSearchSuggestionDao(database: AppDatabase): SearchSuggestionDao = database.searchSuggestionDao()

    @Provides
    fun providesAdminWordDao(database: AppDatabase): AdminWordDao = database.adminWordDao()

    @Provides
    fun provideInAppropriateWordDao(database: AppDatabase): InappropriateWordDao = database.inappropriateWordDao()
}