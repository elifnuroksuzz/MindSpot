package com.example.mindspotapp.di

import android.content.Context
import androidx.room.Room
import com.example.mindspotapp.data.dao.MoodDao
import com.example.mindspotapp.data.database.MoodDatabase
import com.example.mindspotapp.utils.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Dependency Injection modülü
 * Database ve DAO bağımlılıklarını sağlar
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * MoodDatabase instance'ını sağlar
     * Singleton olarak yönetilir - uygulama boyunca tek instance kullanılır
     */
    @Provides
    @Singleton
    fun provideMoodDatabase(@ApplicationContext context: Context): MoodDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            MoodDatabase::class.java,
            MoodDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // Development için - production'da migration yazılmalı
            .build()
    }

    /**
     * MoodDao instance'ını sağlar
     * Database'den dao'yu alır
     */
    @Provides
    fun provideMoodDao(database: MoodDatabase): MoodDao {
        return database.moodDao()
    }

    /**
     * PreferencesHelper instance'ını sağlar
     */
    @Provides
    @Singleton
    fun providePreferencesHelper(@ApplicationContext context: Context): PreferencesHelper {
        return PreferencesHelper(context)
    }
}