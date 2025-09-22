package com.example.mindspotapp.presentation.navigation

/**
 * Uygulamadaki ekranların navigation route'larını tanımlar
 */
sealed class Screen(val route: String) {

    /**
     * Onboarding tutorial ekranı
     */
    object Onboarding : Screen("onboarding")

    /**
     * Bildirim izni ekranı
     */
    object NotificationPermission : Screen("notification_permission")

    /**
     * Ana ruh hali kayıt ekranı
     */
    object MoodEntry : Screen("mood_entry")

    /**
     * Ruh hali özet ve istatistik ekranı
     */
    object Summary : Screen("summary")

    /**
     * Ayarlar ve kişiselleştirme ekranı
     */
    object Settings : Screen("settings")
}