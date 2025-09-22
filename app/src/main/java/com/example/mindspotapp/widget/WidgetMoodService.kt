package com.example.mindspotapp.widget

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import com.example.mindspotapp.data.database.MoodDatabase
import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.data.model.MoodType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Widget'tan gelen ruh hali kayıtlarını işleyen service
 */
class WidgetMoodService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val moodId = intent?.getIntExtra(MoodQuickEntryWidget.EXTRA_MOOD_ID, 3) ?: 3

        serviceScope.launch {
            try {
                saveMoodToDatabase(moodId)
                showSuccessMessage(moodId)
            } catch (e: Exception) {
                showErrorMessage()
            } finally {
                stopSelf(startId)
            }
        }

        return START_NOT_STICKY
    }

    /**
     * Ruh hali kaydını veritabanına ekler
     */
    private suspend fun saveMoodToDatabase(moodId: Int) {
        val database = MoodDatabase.getDatabase(this)
        val moodEntry = MoodEntry(
            moodId = moodId,
            timestamp = System.currentTimeMillis(),
            optionalTag = null // Widget'tan etiket ekleme yok
        )

        database.moodDao().insertMood(moodEntry)
    }

    /**
     * Başarı mesajı gösterir
     */
    private fun showSuccessMessage(moodId: Int) {
        val moodType = MoodType.fromId(moodId)
        val message = "Ruh halin kaydedildi: ${moodType?.emoji} ${moodType?.displayName}"

        // Ana thread'de toast göster
        val handler = android.os.Handler(mainLooper)
        handler.post {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Hata mesajı gösterir
     */
    private fun showErrorMessage() {
        val handler = android.os.Handler(mainLooper)
        handler.post {
            Toast.makeText(this, "Kayıt başarısız oldu", Toast.LENGTH_SHORT).show()
        }
    }
}