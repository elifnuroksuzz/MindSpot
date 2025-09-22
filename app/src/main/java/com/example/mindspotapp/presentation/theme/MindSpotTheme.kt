package com.example.mindspotapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.mindspotapp.utils.PreferencesHelper

/**
 * MindSpot uygulamasının renk paleti - güncellenmiş
 */
object MindSpotColors {
    // Ana renkler (varsayılan tema için)
    val EmeraldGreen = Color(0xFF4CAF50)      // Birincil (Vurgu)
    val DeepBlue = Color(0xFF2196F3)          // İkincil (Nötr)
    val LightGray = Color(0xFFF5F5F5)         // Arka Plan
    val White = Color(0xFFFFFFFF)             // Arka Plan
    val DarkGray = Color(0xFF333333)          // Metin

    // Ruh hali renkleri (tema bağımsız)
    val MoodGreat = Color(0xFF4CAF50)         // Harika - Zümrüt Yeşili
    val MoodGood = Color(0xFF8BC34A)          // İyi - Açık Yeşil
    val MoodNormal = Color(0xFFFFEB3B)        // Normal - Sarı
    val MoodBad = Color(0xFFFF9800)           // Kötü - Turuncu
    val MoodVeryBad = Color(0xFFF44336)       // Çok Kötü - Kırmızı
}

/**
 * MindSpot uygulamasının ana tema bileşeni - dinamik tema desteği ile
 */
@Composable
fun MindSpotTheme(
    content: @Composable () -> Unit
) {
    val themeState = rememberThemeState()
    val systemDarkTheme = isSystemInDarkTheme()

    // Color scheme'i belirle
    val colorScheme = ThemeColors.getColorScheme(
        theme = themeState.currentTheme,
        isDark = themeState.isDarkMode || systemDarkTheme
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

/**
 * Dinamik tema güncellemesi için - her değişiklikte recompose olur
 */
@Composable
fun rememberThemeState(): ThemeState {
    val context = LocalContext.current
    val preferencesHelper = remember { PreferencesHelper(context) }

    // Key ile recomposition'ı tetiklemek için
    var themeKey by remember { mutableStateOf(0) }

    val currentTheme = remember(themeKey) {
        AppTheme.values().find {
            it.name == preferencesHelper.getSelectedTheme()
        } ?: AppTheme.MINDSPOT_DEFAULT
    }

    val isDarkMode = remember(themeKey) {
        preferencesHelper.isDarkModeEnabled()
    }

    return ThemeState(
        currentTheme = currentTheme,
        isDarkMode = isDarkMode,
        setTheme = { theme ->
            preferencesHelper.setSelectedTheme(theme.name)
            themeKey++ // Recomposition'ı tetikle
        },
        setDarkMode = { enabled ->
            preferencesHelper.setDarkModeEnabled(enabled)
            themeKey++ // Recomposition'ı tetikle
        }
    )
}

/**
 * Tema state yönetimi
 */
data class ThemeState(
    val currentTheme: AppTheme,
    val isDarkMode: Boolean,
    val setTheme: (AppTheme) -> Unit,
    val setDarkMode: (Boolean) -> Unit
)