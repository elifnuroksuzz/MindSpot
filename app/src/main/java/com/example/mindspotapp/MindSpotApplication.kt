package com.example.mindspotapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * MindSpot uygulamasının ana Application sınıfı
 * Hilt dependency injection sistemini başlatır
 */
@HiltAndroidApp
class MindSpotApplication : Application()