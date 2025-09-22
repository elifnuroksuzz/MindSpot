package com.example.mindspotapp.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Haptic feedback yönetimi için helper sınıf
 * Premium dokunuş hissi sağlar
 */
class HapticFeedbackHelper(private val context: Context) {

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    /**
     * Hafif dokunuş feedback'i - buton tıklamaları için
     */
    fun lightImpact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(50)
        }
    }

    /**
     * Orta şiddette feedback - önemli aksiyonlar için
     */
    fun mediumImpact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(100, 180)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(100)
        }
    }

    /**
     * Güçlü feedback - başarılı kayıt, onay işlemleri için
     */
    fun strongImpact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(150, 255)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(150)
        }
    }

    /**
     * Çift titreşim - başarı bildirimi için
     */
    fun successPattern() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 80, 50, 80)
            val amplitudes = intArrayOf(0, 180, 0, 180)
            vibrator?.vibrate(
                VibrationEffect.createWaveform(pattern, amplitudes, -1)
            )
        } else {
            @Suppress("DEPRECATION")
            val pattern = longArrayOf(0, 80, 50, 80)
            vibrator?.vibrate(pattern, -1)
        }
    }

    /**
     * Hata feedback'i - kısa kesik titreşimler
     */
    fun errorPattern() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pattern = longArrayOf(0, 30, 20, 30, 20, 30)
            val amplitudes = intArrayOf(0, 150, 0, 150, 0, 150)
            vibrator?.vibrate(
                VibrationEffect.createWaveform(pattern, amplitudes, -1)
            )
        } else {
            @Suppress("DEPRECATION")
            val pattern = longArrayOf(0, 30, 20, 30, 20, 30)
            vibrator?.vibrate(pattern, -1)
        }
    }

    /**
     * Seçim feedback'i - ruh hali seçimi için
     */
    fun selectionFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(40, 120)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(40)
        }
    }
}

/**
 * Compose için haptic feedback helper'ı hatırla
 */
@Composable
fun rememberHapticFeedback(): HapticFeedbackHelper {
    val context = LocalContext.current
    return remember { HapticFeedbackHelper(context) }
}