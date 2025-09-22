package com.example.mindspotapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindspotapp.presentation.navigation.Screen
import com.example.mindspotapp.presentation.screens.mood_entry.MoodEntryScreen
import com.example.mindspotapp.presentation.screens.onboarding.OnboardingScreen
import com.example.mindspotapp.presentation.screens.permissions.NotificationPermissionScreen
import com.example.mindspotapp.presentation.screens.settings.SettingsScreen
import com.example.mindspotapp.presentation.screens.summary.SummaryScreen
import com.example.mindspotapp.presentation.theme.MindSpotTheme
import com.example.mindspotapp.utils.PreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MindSpot uygulamasının ana aktivitesi
 * Compose UI ve Navigation sistemini yönetir
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MindSpotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // İlk kullanım kontrolü
                    val isFirstLaunch = preferencesHelper.isFirstLaunch()
                    val startDestination = if (isFirstLaunch) {
                        Screen.Onboarding.route
                    } else {
                        Screen.MoodEntry.route
                    }

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        // Onboarding ekranı
                        composable(Screen.Onboarding.route) {
                            OnboardingScreen(
                                navController = navController,
                                onOnboardingComplete = {
                                    preferencesHelper.setOnboardingCompleted()
                                }
                            )
                        }

                        // Bildirim izni ekranı
                        composable(Screen.NotificationPermission.route) {
                            NotificationPermissionScreen(
                                navController = navController,
                                onPermissionHandled = {
                                    // Bildirim izni işlendi
                                }
                            )
                        }

                        // Ruh hali giriş ekranı
                        composable(Screen.MoodEntry.route) {
                            MoodEntryScreen(navController = navController)
                        }

                        // Özet ekranı
                        composable(Screen.Summary.route) {
                            SummaryScreen(navController = navController)
                        }

                        // Ayarlar ekranı
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}