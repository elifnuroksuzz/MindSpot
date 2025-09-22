package com.example.mindspotapp.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.mindspotapp.MainActivity
import com.example.mindspotapp.R
import com.example.mindspotapp.data.model.MoodType

/**
 * MindSpot hızlı ruh hali kayıt widget'ı
 * Ana ekranda 5 ruh hali butonunu gösterir
 */
class MoodQuickEntryWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_MOOD_SELECTED = "com.example.mindspotapp.MOOD_SELECTED"
        const val EXTRA_MOOD_ID = "mood_id"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_MOOD_SELECTED) {
            val moodId = intent.getIntExtra(EXTRA_MOOD_ID, 3)
            handleMoodSelection(context, moodId)
        }
    }

    /**
     * Widget'ı günceller
     */
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_mood_quick_entry)

        // Başlık
        views.setTextViewText(R.id.widget_title, "MindSpot")
        views.setTextViewText(R.id.widget_subtitle, "Bugün nasıl hissediyorsun?")

        // Her ruh hali butonu için click listener'lar
        setupMoodButton(context, views, R.id.btn_mood_great, MoodType.GREAT.id, "😄")
        setupMoodButton(context, views, R.id.btn_mood_good, MoodType.GOOD.id, "😊")
        setupMoodButton(context, views, R.id.btn_mood_normal, MoodType.NORMAL.id, "😐")
        setupMoodButton(context, views, R.id.btn_mood_bad, MoodType.BAD.id, "😔")
        setupMoodButton(context, views, R.id.btn_mood_very_bad, MoodType.VERY_BAD.id, "😢")

        // Ana uygulamayı açma butonu
        val openAppIntent = Intent(context, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(
            context, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_container, openAppPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    /**
     * Ruh hali butonunu ayarlar
     */
    private fun setupMoodButton(
        context: Context,
        views: RemoteViews,
        buttonId: Int,
        moodId: Int,
        emoji: String
    ) {
        // Butona emoji yazısını ekle
        views.setTextViewText(buttonId, emoji)

        // Click intent'i oluştur
        val intent = Intent(context, MoodQuickEntryWidget::class.java).apply {
            action = ACTION_MOOD_SELECTED
            putExtra(EXTRA_MOOD_ID, moodId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            moodId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        views.setOnClickPendingIntent(buttonId, pendingIntent)
    }

    /**
     * Ruh hali seçimini işler
     */
    private fun handleMoodSelection(context: Context, moodId: Int) {
        // Veritabanına kaydet
        val intent = Intent(context, WidgetMoodService::class.java).apply {
            putExtra(EXTRA_MOOD_ID, moodId)
        }
        context.startService(intent)

        // Kullanıcıya bildirim göster (opsiyonel)
        showMoodSavedNotification(context, moodId)
    }

    /**
     * Kayıt başarılı bildirimi gösterir
     */
    private fun showMoodSavedNotification(context: Context, moodId: Int) {
        val moodType = MoodType.fromId(moodId)
        val notificationHelper = com.example.mindspotapp.utils.NotificationHelper(context)

        // Basit bir başarı bildirimi (opsiyonel)
        // notificationHelper.sendMoodSavedNotification(moodType?.displayName ?: "Bilinmeyen")
    }
}