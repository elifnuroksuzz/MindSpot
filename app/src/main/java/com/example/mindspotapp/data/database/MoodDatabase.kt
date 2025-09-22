package com.example.mindspotapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.mindspotapp.data.dao.MoodDao
import com.example.mindspotapp.data.model.MoodEntry

/**
 * MindSpot uygulamasının ana Room database sınıfı
 * Offline-first yaklaşımı için lokal veritabanı yönetimi
 */
@Database(
    entities = [MoodEntry::class],
    version = 1,
    exportSchema = false
)
abstract class MoodDatabase : RoomDatabase() {

    /**
     * MoodDao instance'ını sağlar
     */
    abstract fun moodDao(): MoodDao

    companion object {
        /**
         * Database ismi
         */
        const val DATABASE_NAME = "mindspot_database"

        /**
         * Singleton instance (Hilt tarafından yönetilecek)
         */
        @Volatile
        private var INSTANCE: MoodDatabase? = null

        /**
         * Database instance'ını oluşturur veya mevcut instance'ı döndürür
         * Not: Bu fonksiyon Hilt modülü tarafından kullanılacak
         */
        fun getDatabase(context: Context): MoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Development için - production'da migration kullanılmalı
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}