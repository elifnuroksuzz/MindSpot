package com.example.mindspotapp.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.mindspotapp.utils.PreferencesHelper

/**
 * SharedPreferences yönetimi için helper sınıf
 */
@Singleton
class PreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        Constants.SHARED_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    /**
     * İlk kullanım durumunu kontrol eder
     */
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(Constants.KEY_FIRST_LAUNCH, true)
    }

    /**
     * İlk kullanım tamamlandığını işaretler
     */
    fun setOnboardingCompleted() {
        prefs.edit()
            .putBoolean(Constants.KEY_FIRST_LAUNCH, false)
            .apply()
    }

    /**
     * Bildirim durumunu kontrol eder
     */
    fun isNotificationEnabled(): Boolean {
        return prefs.getBoolean(Constants.KEY_NOTIFICATION_ENABLED, true)
    }

    /**
     * Bildirim durumunu günceller
     */
    fun setNotificationEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(Constants.KEY_NOTIFICATION_ENABLED, enabled)
            .apply()
    }

    /**
     * Bildirim zamanını alır (varsayılan: 20:00)
     */
    fun getNotificationTime(): String {
        return prefs.getString(Constants.KEY_NOTIFICATION_TIME, "20:00") ?: "20:00"
    }

    /**
     * Bildirim zamanını günceller
     */
    fun setNotificationTime(time: String) {
        prefs.edit()
            .putString(Constants.KEY_NOTIFICATION_TIME, time)
            .apply()
    }

    /**
     * Seçili temayı alır
     */
    fun getSelectedTheme(): String {
        return prefs.getString(Constants.KEY_SELECTED_THEME, "MINDSPOT_DEFAULT") ?: "MINDSPOT_DEFAULT"
    }

    /**
     * Seçili temayı günceller
     */
    fun setSelectedTheme(theme: String) {
        prefs.edit()
            .putString(Constants.KEY_SELECTED_THEME, theme)
            .apply()
    }

    /**
     * Karanlık mod durumunu kontrol eder
     */
    fun isDarkModeEnabled(): Boolean {
        return prefs.getBoolean(Constants.KEY_DARK_MODE, false)
    }

    /**
     * Karanlık mod durumunu günceller
     */
    fun setDarkModeEnabled(enabled: Boolean) {
        prefs.edit()
            .putBoolean(Constants.KEY_DARK_MODE, enabled)
            .apply()
    }

    /**
     * Özel etiketleri alır
     */
    fun getCustomTags(): Set<String> {
        return prefs.getStringSet(Constants.KEY_CUSTOM_TAGS, emptySet()) ?: emptySet()
    }

    /**
     * Özel etiket ekler
     */
    fun addCustomTag(tag: String) {
        val currentTags = getCustomTags().toMutableSet()
        currentTags.add(tag)
        prefs.edit()
            .putStringSet(Constants.KEY_CUSTOM_TAGS, currentTags)
            .apply()
    }

    /**
     * Özel etiket siler
     */
    fun removeCustomTag(tag: String) {
        val currentTags = getCustomTags().toMutableSet()
        currentTags.remove(tag)
        prefs.edit()
            .putStringSet(Constants.KEY_CUSTOM_TAGS, currentTags)
            .apply()
    }
}