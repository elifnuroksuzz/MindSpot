package com.example.mindspotapp.di

import com.example.mindspotapp.data.repository.MoodRepositoryImpl
import com.example.mindspotapp.domain.repository.MoodRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Repository bağımlılıklarını yöneten Hilt modülü
 * Interface'leri implementation'larına bağlar
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * MoodRepository interface'ini MoodRepositoryImpl'e bağlar
     */
    @Binds
    @Singleton
    abstract fun bindMoodRepository(
        moodRepositoryImpl: MoodRepositoryImpl
    ): MoodRepository
}