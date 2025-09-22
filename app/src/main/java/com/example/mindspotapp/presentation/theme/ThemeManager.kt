package com.example.mindspotapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mindspotapp.utils.PreferencesHelper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tema yönetimi ve kişiselleştirme sistemi
 */
@Singleton
class ThemeManager @Inject constructor(
    private val preferencesHelper: PreferencesHelper
) {

    /**
     * Mevcut tema tipini alır
     */
    fun getCurrentTheme(): AppTheme {
        val themeName = preferencesHelper.getSelectedTheme()
        return AppTheme.values().find { it.name == themeName } ?: AppTheme.MINDSPOT_DEFAULT
    }

    /**
     * Tema tipini değiştirir
     */
    fun setTheme(theme: AppTheme) {
        preferencesHelper.setSelectedTheme(theme.name)
    }

    /**
     * Karanlık mod durumunu alır
     */
    fun isDarkModeEnabled(): Boolean {
        return preferencesHelper.isDarkModeEnabled()
    }

    /**
     * Karanlık mod durumunu değiştirir
     */
    fun setDarkMode(enabled: Boolean) {
        preferencesHelper.setDarkModeEnabled(enabled)
    }
}

/**
 * Uygulama tema tipleri
 */
enum class AppTheme(
    val displayName: String,
    val emoji: String,
    val description: String
) {
    MINDSPOT_DEFAULT(
        displayName = "MindSpot Klasik",
        emoji = "🎯",
        description = "Varsayılan yeşil-mavi tema"
    ),
    WARM_SUNSET(
        displayName = "Sıcak Günbatımı",
        emoji = "🌅",
        description = "Turuncu-pembe sıcak tonlar"
    ),
    COOL_OCEAN(
        displayName = "Sakin Okyanus",
        emoji = "🌊",
        description = "Mavi-teal soğuk tonlar"
    ),
    PURPLE_DREAM(
        displayName = "Mor Rüya",
        emoji = "🔮",
        description = "Mor-lavanta tonları"
    ),
    FOREST_GREEN(
        displayName = "Orman Yeşili",
        emoji = "🌲",
        description = "Doğal yeşil tonları"
    )
}

/**
 * Tema renklerini yönetir
 */
object ThemeColors {

    // MindSpot Default
    val MindSpotLight = lightColorScheme(
        primary = Color(0xFF4CAF50),
        secondary = Color(0xFF2196F3),
        background = Color.White,
        surface = Color(0xFFF5F5F5),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF333333),
        onSurface = Color(0xFF333333)
    )

    val MindSpotDark = darkColorScheme(
        primary = Color(0xFF4CAF50),
        secondary = Color(0xFF2196F3),
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color.White,
        onSurface = Color.White
    )

    // Warm Sunset
    val WarmSunsetLight = lightColorScheme(
        primary = Color(0xFFFF6B35),
        secondary = Color(0xFFFF9068),
        background = Color(0xFFFFF8F3),
        surface = Color(0xFFFFF0E6),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF2D1810),
        onSurface = Color(0xFF2D1810)
    )

    val WarmSunsetDark = darkColorScheme(
        primary = Color(0xFFFF6B35),
        secondary = Color(0xFFFF9068),
        background = Color(0xFF1A1008),
        surface = Color(0xFF2D1810),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFFFFF8F3),
        onSurface = Color(0xFFFFF8F3)
    )

    // Cool Ocean
    val CoolOceanLight = lightColorScheme(
        primary = Color(0xFF00BCD4),
        secondary = Color(0xFF4DD0E1),
        background = Color(0xFFF0FDFF),
        surface = Color(0xFFE0F7FA),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF0D3B42),
        onSurface = Color(0xFF0D3B42)
    )

    val CoolOceanDark = darkColorScheme(
        primary = Color(0xFF00BCD4),
        secondary = Color(0xFF4DD0E1),
        background = Color(0xFF0A1D21),
        surface = Color(0xFF0D3B42),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFFF0FDFF),
        onSurface = Color(0xFFF0FDFF)
    )

    // Purple Dream
    val PurpleDreamLight = lightColorScheme(
        primary = Color(0xFF9C27B0),
        secondary = Color(0xFFBA68C8),
        background = Color(0xFFFCF8FF),
        surface = Color(0xFFF3E5F5),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF2A1A2E),
        onSurface = Color(0xFF2A1A2E)
    )

    val PurpleDreamDark = darkColorScheme(
        primary = Color(0xFF9C27B0),
        secondary = Color(0xFFBA68C8),
        background = Color(0xFF1A0E1D),
        surface = Color(0xFF2A1A2E),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFFFCF8FF),
        onSurface = Color(0xFFFCF8FF)
    )

    // Forest Green
    val ForestGreenLight = lightColorScheme(
        primary = Color(0xFF2E7D32),
        secondary = Color(0xFF66BB6A),
        background = Color(0xFFF8FFF8),
        surface = Color(0xFFE8F5E8),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF1B3B1C),
        onSurface = Color(0xFF1B3B1C)
    )

    val ForestGreenDark = darkColorScheme(
        primary = Color(0xFF2E7D32),
        secondary = Color(0xFF66BB6A),
        background = Color(0xFF0F1B10),
        surface = Color(0xFF1B3B1C),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFFF8FFF8),
        onSurface = Color(0xFFF8FFF8)
    )

    /**
     * Seçilen tema ve mod için ColorScheme döndürür
     */
    fun getColorScheme(theme: AppTheme, isDark: Boolean): ColorScheme {
        return when (theme) {
            AppTheme.MINDSPOT_DEFAULT -> if (isDark) MindSpotDark else MindSpotLight
            AppTheme.WARM_SUNSET -> if (isDark) WarmSunsetDark else WarmSunsetLight
            AppTheme.COOL_OCEAN -> if (isDark) CoolOceanDark else CoolOceanLight
            AppTheme.PURPLE_DREAM -> if (isDark) PurpleDreamDark else PurpleDreamLight
            AppTheme.FOREST_GREEN -> if (isDark) ForestGreenDark else ForestGreenLight
        }
    }
}