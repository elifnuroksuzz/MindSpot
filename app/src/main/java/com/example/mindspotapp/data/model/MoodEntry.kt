package com.example.mindspotapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Ruh hali kayıt verisi için veri modeli
 * Room database entity olarak kullanılır
 */
@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * Ruh hali ID'si (1-5 arası)
     * 1: Çok Kötü, 2: Kötü, 3: Normal, 4: İyi, 5: Harika
     */
    val moodId: Int,

    /**
     * Kayıt zamanı (Unix timestamp)
     */
    val timestamp: Long,

    /**
     * Opsiyonel bağlam etiketi
     * Örn: "İş", "Aile", "Arkadaş", "Uyku", "Spor"
     */
    val optionalTag: String? = null
)

/**
 * Ruh hali türleri enum sınıfı
 */
enum class MoodType(val id: Int, val displayName: String, val emoji: String) {
    VERY_BAD(1, "Çok Kötü", "😢"),
    BAD(2, "Kötü", "😔"),
    NORMAL(3, "Normal", "😐"),
    GOOD(4, "İyi", "😊"),
    GREAT(5, "Harika", "😄");

    companion object {
        /**
         * ID'ye göre MoodType döndürür
         */
        fun fromId(id: Int): MoodType? {
            return values().find { it.id == id }
        }

        /**
         * Tüm ruh hali seçeneklerini liste olarak döndürür
         */
        fun getAllMoods(): List<MoodType> {
            return values().toList()
        }
    }
}