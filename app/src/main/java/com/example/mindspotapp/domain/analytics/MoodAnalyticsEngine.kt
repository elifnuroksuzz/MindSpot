package com.example.mindspotapp.domain.analytics

import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.data.model.MoodType
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ruh hali verilerini analiz eden akıllı motor
 * Desenler, korelasyonlar ve içgörüler üretir
 */
@Singleton
class MoodAnalyticsEngine @Inject constructor() {

    /**
     * Comprehensive mood analysis yapar
     */
    fun analyzeMoodData(entries: List<MoodEntry>): MoodAnalysisResult {
        if (entries.isEmpty()) return MoodAnalysisResult()

        return MoodAnalysisResult(
            patterns = detectPatterns(entries),
            insights = generateInsights(entries),
            correlations = analyzeTagCorrelations(entries),
            trends = analyzeTrends(entries),
            recommendations = generateRecommendations(entries)
        )
    }

    /**
     * Haftalık ve günlük desenleri tespit eder
     */
    private fun detectPatterns(entries: List<MoodEntry>): List<MoodPattern> {
        val patterns = mutableListOf<MoodPattern>()

        // Günlük desenler
        val dailyPatterns = analyzeDailyPatterns(entries)
        patterns.addAll(dailyPatterns)

        // Haftalık desenler
        val weeklyPatterns = analyzeWeeklyPatterns(entries)
        patterns.addAll(weeklyPatterns)

        // Etiket bazlı desenler
        val tagPatterns = analyzeTagPatterns(entries)
        patterns.addAll(tagPatterns)

        return patterns
    }

    /**
     * Günlük ruh hali desenlerini analiz eder
     */
    private fun analyzeDailyPatterns(entries: List<MoodEntry>): List<MoodPattern> {
        val patterns = mutableListOf<MoodPattern>()
        val calendar = Calendar.getInstance()

        // Son 30 günlük verileri analiz et
        val recentEntries = entries.filter {
            val thirtyDaysAgo = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L)
            it.timestamp >= thirtyDaysAgo
        }

        if (recentEntries.size < 7) return patterns

        // Sabah/akşam farkları
        val morningEntries = recentEntries.filter { entry ->
            calendar.timeInMillis = entry.timestamp
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            hour in 6..12
        }

        val eveningEntries = recentEntries.filter { entry ->
            calendar.timeInMillis = entry.timestamp
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            hour in 18..23
        }

        if (morningEntries.size >= 3 && eveningEntries.size >= 3) {
            val morningAvg = morningEntries.map { it.moodId }.average()
            val eveningAvg = eveningEntries.map { it.moodId }.average()

            when {
                morningAvg > eveningAvg + 0.5 -> {
                    patterns.add(
                        MoodPattern(
                            type = PatternType.DAILY_CYCLE,
                            description = "Sabahları genellikle daha iyi hissediyorsun",
                            confidence = calculateConfidence(morningEntries.size + eveningEntries.size),
                            actionable = true,
                            suggestion = "Önemli kararları sabah saatlerinde almayı dene"
                        )
                    )
                }
                eveningAvg > morningAvg + 0.5 -> {
                    patterns.add(
                        MoodPattern(
                            type = PatternType.DAILY_CYCLE,
                            description = "Akşamları daha enerjik ve pozitif hissediyorsun",
                            confidence = calculateConfidence(morningEntries.size + eveningEntries.size),
                            actionable = true,
                            suggestion = "Yaratıcı aktivitelerini akşam saatlerine planla"
                        )
                    )
                }
            }
        }

        return patterns
    }

    /**
     * Haftalık desenler analizi
     */
    private fun analyzeWeeklyPatterns(entries: List<MoodEntry>): List<MoodPattern> {
        val patterns = mutableListOf<MoodPattern>()
        val calendar = Calendar.getInstance()

        // Son 4 haftalık verileri al
        val monthlyEntries = entries.filter {
            val monthAgo = System.currentTimeMillis() - (28 * 24 * 60 * 60 * 1000L)
            it.timestamp >= monthAgo
        }

        if (monthlyEntries.size < 14) return patterns

        // Haftanın günlerine göre grupla
        val dayOfWeekMoods = monthlyEntries.groupBy { entry ->
            calendar.timeInMillis = entry.timestamp
            calendar.get(Calendar.DAY_OF_WEEK)
        }

        // Her günün ortalama ruh halini hesapla
        val dayAverages = dayOfWeekMoods.mapValues { (_, entries) ->
            entries.map { it.moodId }.average()
        }

        // En iyi ve en kötü günleri bul
        val bestDay = dayAverages.maxByOrNull { it.value }
        val worstDay = dayAverages.minByOrNull { it.value }

        if (bestDay != null && worstDay != null && bestDay.value - worstDay.value > 0.7) {
            val bestDayName = getDayName(bestDay.key)
            val worstDayName = getDayName(worstDay.key)

            patterns.add(
                MoodPattern(
                    type = PatternType.WEEKLY_CYCLE,
                    description = "$bestDayName günlerin en iyi, $worstDayName günlerin en zor geçiyor",
                    confidence = calculateConfidence(monthlyEntries.size),
                    actionable = true,
                    suggestion = "$worstDayName günleri için öz-bakım aktiviteleri planla"
                )
            )
        }

        return patterns
    }

    /**
     * Etiket bazlı desen analizi
     */
    private fun analyzeTagPatterns(entries: List<MoodEntry>): List<MoodPattern> {
        val patterns = mutableListOf<MoodPattern>()

        // Etiketli kayıtları grupla
        val taggedEntries = entries.filter { !it.optionalTag.isNullOrBlank() }
        if (taggedEntries.size < 5) return patterns

        val tagMoods = taggedEntries.groupBy { it.optionalTag!! }

        tagMoods.forEach { (tag, tagEntries) ->
            if (tagEntries.size >= 3) {
                val avgMood = tagEntries.map { it.moodId }.average()

                when {
                    avgMood >= 4.0 -> {
                        patterns.add(
                            MoodPattern(
                                type = PatternType.TAG_CORRELATION,
                                description = "$tag ile ilgili durumlar seni mutlu ediyor",
                                confidence = calculateConfidence(tagEntries.size),
                                actionable = true,
                                suggestion = "$tag aktivitelerine daha fazla zaman ayır"
                            )
                        )
                    }
                    avgMood <= 2.5 -> {
                        patterns.add(
                            MoodPattern(
                                type = PatternType.TAG_CORRELATION,
                                description = "$tag durumları ruh halini olumsuz etkiliyor",
                                confidence = calculateConfidence(tagEntries.size),
                                actionable = true,
                                suggestion = "$tag ile ilgili stresi azaltma yolları dene"
                            )
                        )
                    }
                }
            }
        }

        return patterns
    }

    /**
     * Etiket korelasyonlarını analiz eder
     */
    private fun analyzeTagCorrelations(entries: List<MoodEntry>): List<TagCorrelation> {
        val correlations = mutableListOf<TagCorrelation>()
        val taggedEntries = entries.filter { !it.optionalTag.isNullOrBlank() }

        val tagMoods = taggedEntries.groupBy { it.optionalTag!! }

        tagMoods.forEach { (tag, tagEntries) ->
            if (tagEntries.size >= 3) {
                val avgMood = tagEntries.map { it.moodId }.average()
                val impact = when {
                    avgMood >= 4.0 -> CorrelationImpact.VERY_POSITIVE
                    avgMood >= 3.5 -> CorrelationImpact.POSITIVE
                    avgMood <= 2.0 -> CorrelationImpact.VERY_NEGATIVE
                    avgMood <= 2.5 -> CorrelationImpact.NEGATIVE
                    else -> CorrelationImpact.NEUTRAL
                }

                correlations.add(
                    TagCorrelation(
                        tag = tag,
                        averageMood = avgMood.toFloat(),
                        entryCount = tagEntries.size,
                        impact = impact
                    )
                )
            }
        }

        return correlations.sortedByDescending { it.averageMood }
    }

    /**
     * Trend analizi yapar
     */
    private fun analyzeTrends(entries: List<MoodEntry>): TrendAnalysis {
        if (entries.size < 7) return TrendAnalysis()

        val sortedEntries = entries.sortedBy { it.timestamp }
        val recent = sortedEntries.takeLast(7)
        val previous = sortedEntries.dropLast(7).takeLast(7)

        if (previous.isEmpty()) return TrendAnalysis()

        val recentAvg = recent.map { it.moodId }.average()
        val previousAvg = previous.map { it.moodId }.average()
        val change = recentAvg - previousAvg

        val direction = when {
            change > 0.3 -> TrendDirection.IMPROVING
            change < -0.3 -> TrendDirection.DECLINING
            else -> TrendDirection.STABLE
        }

        return TrendAnalysis(
            direction = direction,
            changeAmount = change.toFloat(),
            confidence = calculateConfidence(recent.size + previous.size)
        )
    }

    /**
     * Kişiselleştirilmiş öneriler üretir
     */
    private fun generateRecommendations(entries: List<MoodEntry>): List<String> {
        val recommendations = mutableListOf<String>()

        if (entries.isEmpty()) return recommendations

        val recentAvg = entries.takeLast(7).map { it.moodId }.average()
        val overallAvg = entries.map { it.moodId }.average()

        // Genel durum önerileri
        when {
            recentAvg < 2.5 -> {
                recommendations.add("Son günlerde zorlanıyor gibisin. Kendinle nazik ol ve destek al.")
                recommendations.add("Küçük, başarılabilir hedefler koy ve kendini ödüllendir.")
            }
            recentAvg > 4.0 -> {
                recommendations.add("Harika bir dönemdesin! Bu pozitif enerjiyi koruyacak aktiviteleri not al.")
                recommendations.add("İyi günlerindeki alışkanlıklarını gelecekte tekrarlayabilirsin.")
            }
            overallAvg > recentAvg + 0.5 -> {
                recommendations.add("Normalde daha iyi hissediyorsun. Geçmişte seni mutlu eden şeyleri hatırla.")
            }
        }

        // Kayıt sıklığı önerileri
        val daysSinceLastEntry = (System.currentTimeMillis() - entries.last().timestamp) / (24 * 60 * 60 * 1000)
        if (daysSinceLastEntry >= 3) {
            recommendations.add("Düzenli kayıt yapmak daha iyi içgörüler sağlar. Günlük hatırlatıcıları aktif et.")
        }

        return recommendations
    }

    /**
     * İçgörüler üretir
     */
    private fun generateInsights(entries: List<MoodEntry>): List<String> {
        val insights = mutableListOf<String>()

        if (entries.size < 7) {
            insights.add("Daha detaylı analizler için daha fazla veri toplamaya devam et.")
            return insights
        }

        val overallAvg = entries.map { it.moodId }.average()
        val totalDays = entries.groupBy {
            val cal = Calendar.getInstance()
            cal.timeInMillis = it.timestamp
            "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.DAY_OF_YEAR)}"
        }.size

        insights.add("Toplam $totalDays günde ${entries.size} kayıt yaptın.")
        insights.add("Ortalama ruh halin: ${String.format("%.1f", overallAvg)}/5")

        // Streak analizi
        val streak = calculateCurrentStreak(entries)
        if (streak > 1) {
            insights.add("$streak gündür düzenli kayıt yapıyorsun! Tebrikler.")
        }

        return insights
    }

    // Yardımcı fonksiyonlar

    private fun calculateConfidence(sampleSize: Int): Float {
        return when {
            sampleSize >= 20 -> 0.9f
            sampleSize >= 10 -> 0.7f
            sampleSize >= 5 -> 0.5f
            else -> 0.3f
        }
    }

    private fun getDayName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "Pazartesi"
            Calendar.TUESDAY -> "Salı"
            Calendar.WEDNESDAY -> "Çarşamba"
            Calendar.THURSDAY -> "Perşembe"
            Calendar.FRIDAY -> "Cuma"
            Calendar.SATURDAY -> "Cumartesi"
            Calendar.SUNDAY -> "Pazar"
            else -> "Bilinmeyen"
        }
    }

    private fun calculateCurrentStreak(entries: List<MoodEntry>): Int {
        val sortedEntries = entries.sortedByDescending { it.timestamp }
        if (sortedEntries.isEmpty()) return 0

        val calendar = Calendar.getInstance()
        var streak = 0
        var currentDate = System.currentTimeMillis()

        for (entry in sortedEntries) {
            calendar.timeInMillis = entry.timestamp
            val entryDate = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            calendar.timeInMillis = currentDate
            val checkDate = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            if (entryDate == checkDate) {
                streak++
                currentDate -= 24 * 60 * 60 * 1000L
            } else {
                break
            }
        }

        return streak
    }
}

// Data sınıfları

data class MoodAnalysisResult(
    val patterns: List<MoodPattern> = emptyList(),
    val insights: List<String> = emptyList(),
    val correlations: List<TagCorrelation> = emptyList(),
    val trends: TrendAnalysis = TrendAnalysis(),
    val recommendations: List<String> = emptyList()
)

data class MoodPattern(
    val type: PatternType,
    val description: String,
    val confidence: Float,
    val actionable: Boolean,
    val suggestion: String? = null
)

enum class PatternType {
    DAILY_CYCLE,
    WEEKLY_CYCLE,
    TAG_CORRELATION,
    TREND_CHANGE
}

data class TagCorrelation(
    val tag: String,
    val averageMood: Float,
    val entryCount: Int,
    val impact: CorrelationImpact
)

enum class CorrelationImpact {
    VERY_POSITIVE,
    POSITIVE,
    NEUTRAL,
    NEGATIVE,
    VERY_NEGATIVE
}

data class TrendAnalysis(
    val direction: TrendDirection = TrendDirection.STABLE,
    val changeAmount: Float = 0f,
    val confidence: Float = 0f
)

enum class TrendDirection {
    IMPROVING,
    STABLE,
    DECLINING
}