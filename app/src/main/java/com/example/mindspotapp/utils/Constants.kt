package com.example.mindspotapp.utils

/**
 * Uygulama genelinde kullanılan sabitler
 */
object Constants {

    // Veritabanı
    const val DATABASE_NAME = "mindspot_database"
    const val DATABASE_VERSION = 1

    // Bildirimler
    const val NOTIFICATION_CHANNEL_ID = "mindspot_reminders"
    const val MOOD_REMINDER_NOTIFICATION_ID = 1001

    // Paylaşım
    const val SHARED_PREFS_NAME = "mindspot_preferences"
    const val KEY_FIRST_LAUNCH = "first_launch"
    const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
    const val KEY_NOTIFICATION_TIME = "notification_time"
    const val KEY_SELECTED_THEME = "selected_theme"
    const val KEY_DARK_MODE = "dark_mode"
    const val KEY_CUSTOM_TAGS = "custom_tags"

    // Etiketler
    val DEFAULT_TAGS = listOf(
        "İş",
        "Aile",
        "Arkadaş",
        "Uyku",
        "Spor",
        "Yemek",
        "Okul",
        "Sağlık",
        "Hobiler",
        "Sosyal"
    )

    // İstatistikler
    const val DEFAULT_RECENT_ENTRIES_LIMIT = 10
    const val STATISTICS_DAYS_RANGE = 30

    // UI
    const val ANIMATION_DURATION_SHORT = 300
    const val ANIMATION_DURATION_MEDIUM = 500
    const val ANIMATION_DURATION_LONG = 800
}