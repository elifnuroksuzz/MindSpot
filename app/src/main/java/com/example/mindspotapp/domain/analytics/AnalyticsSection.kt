package com.example.mindspotapp.presentation.components.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindspotapp.domain.analytics.*
import com.example.mindspotapp.presentation.theme.MindSpotColors

/**
 * Akıllı analizler bölümü
 */
@Composable
fun AnalyticsSection(
    analysisResult: MoodAnalysisResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // İçgörüler
        if (analysisResult.insights.isNotEmpty()) {
            InsightsCard(insights = analysisResult.insights)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Desenler
        if (analysisResult.patterns.isNotEmpty()) {
            PatternsCard(patterns = analysisResult.patterns)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Trend analizi
        if (analysisResult.trends.confidence > 0.3f) {
            TrendCard(trends = analysisResult.trends)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Etiket korelasyonları
        if (analysisResult.correlations.isNotEmpty()) {
            CorrelationsCard(correlations = analysisResult.correlations)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Öneriler
        if (analysisResult.recommendations.isNotEmpty()) {
            RecommendationsCard(recommendations = analysisResult.recommendations)
        }
    }
}

/**
 * İçgörüler kartı
 */
@Composable
private fun InsightsCard(insights: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "💡 İçgörüler",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            insights.forEach { insight ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "•",
                        color = MindSpotColors.EmeraldGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = insight,
                        fontSize = 14.sp,
                        color = MindSpotColors.DarkGray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Desenler kartı
 */
@Composable
private fun PatternsCard(patterns: List<MoodPattern>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🔍 Keşfedilen Desenler",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            patterns.forEach { pattern ->
                PatternItem(pattern = pattern)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Tek bir desen öğesi
 */
@Composable
private fun PatternItem(pattern: MoodPattern) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MindSpotColors.LightGray
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pattern.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MindSpotColors.DarkGray,
                    modifier = Modifier.weight(1f)
                )

                ConfidenceBadge(confidence = pattern.confidence)
            }

            pattern.suggestion?.let { suggestion ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💡 $suggestion",
                    fontSize = 12.sp,
                    color = MindSpotColors.DeepBlue,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Güven seviyesi rozeti
 */
@Composable
private fun ConfidenceBadge(confidence: Float) {
    val (color, text) = when {
        confidence >= 0.8f -> MindSpotColors.EmeraldGreen to "Yüksek"
        confidence >= 0.6f -> MindSpotColors.MoodNormal to "Orta"
        else -> Color.Gray to "Düşük"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Trend analizi kartı
 */
@Composable
private fun TrendCard(trends: TrendAnalysis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📈 Trend Analizi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val (icon, color, description) = when (trends.direction) {
                    TrendDirection.IMPROVING -> Triple(
                        Icons.Default.TrendingUp,
                        MindSpotColors.EmeraldGreen,
                        "Ruh halin son zamanlarda iyileşiyor"
                    )
                    TrendDirection.DECLINING -> Triple(
                        Icons.Default.TrendingDown,
                        MindSpotColors.MoodBad,
                        "Son zamanlarda biraz zorlanıyor gibisin"
                    )
                    TrendDirection.STABLE -> Triple(
                        Icons.Default.TrendingFlat,
                        MindSpotColors.DeepBlue,
                        "Ruh halin oldukça stabil"
                    )
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MindSpotColors.DarkGray
                    )

                    if (trends.changeAmount != 0f) {
                        Text(
                            text = "Değişim: ${if (trends.changeAmount > 0) "+" else ""}${String.format("%.1f", trends.changeAmount)} puan",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

/**
 * Etiket korelasyonları kartı
 */
@Composable
private fun CorrelationsCard(correlations: List<TagCorrelation>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🏷️ Etiket Analizi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            correlations.take(5).forEach { correlation ->
                CorrelationItem(correlation = correlation)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Tek bir korelasyon öğesi
 */
@Composable
private fun CorrelationItem(correlation: TagCorrelation) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (emoji, color) = when (correlation.impact) {
            CorrelationImpact.VERY_POSITIVE -> "😄" to MindSpotColors.MoodGreat
            CorrelationImpact.POSITIVE -> "😊" to MindSpotColors.MoodGood
            CorrelationImpact.NEUTRAL -> "😐" to MindSpotColors.MoodNormal
            CorrelationImpact.NEGATIVE -> "😔" to MindSpotColors.MoodBad
            CorrelationImpact.VERY_NEGATIVE -> "😢" to MindSpotColors.MoodVeryBad
        }

        Text(
            text = emoji,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = correlation.tag,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MindSpotColors.DarkGray
            )
            Text(
                text = "${correlation.entryCount} kayıt • Ort: ${String.format("%.1f", correlation.averageMood)}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(color)
        )
    }
}

/**
 * Öneriler kartı
 */
@Composable
private fun RecommendationsCard(recommendations: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MindSpotColors.EmeraldGreen.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "💡 Kişisel Öneriler",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            recommendations.forEach { recommendation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "✨",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = recommendation,
                        fontSize = 14.sp,
                        color = MindSpotColors.DarkGray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}