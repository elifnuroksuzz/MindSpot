package com.example.mindspotapp.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Uygulama ayarlarını ve kullanıcı tercihlerini yöneten sınıf
 * SharedPreferences kullanarak verileri persiste eder
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * Kullanıcının ilk kez uygulamayı açıp açmadığını kontrol eder
     */
    fun isFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_FIRST_LAUNCH, true)
    }

    /**
     * İlk açılış durumunu günceller
     */
    fun setFirstLaunchCompleted() {
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_FIRST_LAUNCH, false)
            .apply()
    }

    /**
     * Bildirim açık/kapalı durumunu kontrol eder
     */
    fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(Constants.KEY_NOTIFICATION_ENABLED, true)
    }

    /**
     * Bildirim ayarını günceller
     */
    fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean(Constants.KEY_NOTIFICATION_ENABLED, enabled)
            .apply()
    }

    /**
     * Bildirim zamanını alır (varsayılan: 20:00)
     */
    fun getNotificationTime(): String {
        return sharedPreferences.getString(Constants.KEY_NOTIFICATION_TIME, "20:00") ?: "20:00"
    }

    /**
     * Bildirim zamanını ayarlar
     */
    fun setNotificationTime(time: String) {
        sharedPreferences.edit()
            .putString(Constants.KEY_NOTIFICATION_TIME, time)
            .apply()
    }

    /**
     * Kullanıcının adını alır
     */
    fun getUserName(): String? {
        return sharedPreferences.getString("user_name", null)
    }

    /**
     * Kullanıcının adını ayarlar
     */
    fun setUserName(name: String) {
        sharedPreferences.edit()
            .putString("user_name", name)
            .apply()
    }

    /**
     * Seçili temayı alır (light/dark/auto)
     */
    fun getThemePreference(): String {
        return sharedPreferences.getString("theme_preference", "auto") ?: "auto"
    }

    /**
     * Tema tercihini ayarlar
     */
    fun setThemePreference(theme: String) {
        sharedPreferences.edit()
            .putString("theme_preference", theme)
            .apply()
    }
}