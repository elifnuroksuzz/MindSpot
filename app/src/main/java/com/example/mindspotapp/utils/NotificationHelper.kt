package com.example.mindspotapp.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mindspotapp.MainActivity
import com.example.mindspotapp.R
import java.util.*

/**
 * Gelişmiş bildirim yönetimi sistemi
 * Akıllı hatırlatıcılar ve motivasyon mesajları
 */
class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "mindspot_reminders"
        const val MOTIVATION_CHANNEL_ID = "mindspot_motivation"
        const val NOTIFICATION_ID = 1001
        const val MOTIVATION_NOTIFICATION_ID = 1002
    }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        createNotificationChannels()
    }

    /**
     * Bildirim kanallarını oluşturur
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Hatırlatıcı kanalı
            val reminderChannel = NotificationChannel(
                CHANNEL_ID,
                "Günlük Hatırlatıcılar",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Günlük ruh hali kaydı hatırlatmaları"
                enableVibration(true)
            }

            // Motivasyon kanalı
            val motivationChannel = NotificationChannel(
                MOTIVATION_CHANNEL_ID,
                "Motivasyon Mesajları",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Pozitif motivasyon mesajları ve içgörüler"
                enableVibration(false)
            }

            notificationManager.createNotificationChannels(listOf(reminderChannel, motivationChannel))
        }
    }

    /**
     * Günlük hatırlatıcı bildirimini gönderir
     */
    fun sendDailyReminder() {
        val messages = listOf(
            "Bugün nasıl hissediyorsun? 🌟",
            "Ruh halini kaydetmeyi unutma! 💚",
            "Kendini dinleme zamanı 🧘",
            "Bugünkü duygularını paylaş 📝",
            "Bir dakikan var mı? Ruh halini kaydet ⏰"
        )

        val randomMessage = messages.random()
        sendNotification(
            title = "MindSpot",
            message = randomMessage,
            channelId = CHANNEL_ID,
            notificationId = NOTIFICATION_ID
        )
    }

    /**
     * Motivasyon bildirimi gönderir
     */
    fun sendMotivationMessage() {
        val motivationMessages = listOf(
            "Duygularını fark etmek güçlü olmaktır! 💪",
            "Her gün biraz daha kendini tanıyorsun 🌱",
            "Ruh hali takibin harika gidiyor! 🎉",
            "Kendine karşı nazik ol bugün 🤗",
            "Duygusal farkındalığın gelişiyor! ✨"
        )

        val randomMotivation = motivationMessages.random()
        sendNotification(
            title = "MindSpot Motivasyon",
            message = randomMotivation,
            channelId = MOTIVATION_CHANNEL_ID,
            notificationId = MOTIVATION_NOTIFICATION_ID
        )
    }

    /**
     * Haftalık özet bildirimi gönderir
     */
    fun sendWeeklySummary(totalEntries: Int, mostFrequentMood: String) {
        val summaryMessage = "Bu hafta $totalEntries kayıt yaptın! En çok $mostFrequentMood hissettin. 📊"

        sendNotification(
            title = "Haftalık Özet",
            message = summaryMessage,
            channelId = MOTIVATION_CHANNEL_ID,
            notificationId = MOTIVATION_NOTIFICATION_ID + 1
        )
    }

    /**
     * Temel bildirim gönderme fonksiyonu
     */
    private fun sendNotification(
        title: String,
        message: String,
        channelId: String,
        notificationId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .build()

        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        } catch (e: SecurityException) {
            // Bildirim izni yok - log'la ama crash etme
        }
    }

    /**
     * Bildirim izni kontrol eder
     */
    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        } else {
            true
        }
    }
}