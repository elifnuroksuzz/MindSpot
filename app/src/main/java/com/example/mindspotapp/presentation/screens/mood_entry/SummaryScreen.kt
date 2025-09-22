package com.example.mindspotapp.presentation.screens.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.data.model.MoodType
import com.example.mindspotapp.presentation.components.analytics.AnalyticsSection
import com.example.mindspotapp.presentation.components.charts.MoodDistributionChart
import com.example.mindspotapp.presentation.components.charts.MoodTrendChart
import com.example.mindspotapp.presentation.theme.MindSpotColors

/**
 * Ruh hali özet ve istatistik ekranı
 * Kayıtları ve analitikleri gösterir
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    navController: NavController,
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Üst bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Geri",
                    tint = MindSpotColors.DarkGray
                )
            }

            Text(
                text = "Ruh Hali Özeti",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (uiState.isLoading) {
                // Loading state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MindSpotColors.EmeraldGreen
                    )
                }
            } else if (uiState.allMoodEntries.isEmpty()) {
                // Empty state
                EmptyStateCard()
            } else {
                // İstatistikler
                StatisticsSection(uiState.moodStatistics)

                Spacer(modifier = Modifier.height(16.dp))

                // Trend grafik
                MoodTrendChart(
                    entries = uiState.recentMoodEntries,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dağılım grafik
                MoodDistributionChart(
                    moodCounts = uiState.moodStatistics.moodDistribution,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Akıllı analizler
                AnalyticsSection(
                    analysisResult = uiState.analysisResult,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Son kayıtlar
                RecentEntriesSection(uiState.allMoodEntries.take(10))
            }

            // Hata mesajı
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        text = error,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * İstatistikler bölümü
 */
@Composable
private fun StatisticsSection(statistics: MoodStatistics) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📊 İstatistikler",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Toplam Kayıt",
                    value = statistics.totalEntries.toString(),
                    color = MindSpotColors.EmeraldGreen
                )

                StatItem(
                    label = "Günlük Seri",
                    value = "${statistics.streak} gün",
                    color = MindSpotColors.DeepBlue
                )

                StatItem(
                    label = "Ortalama",
                    value = String.format("%.1f", statistics.averageMoodScore),
                    color = MindSpotColors.MoodNormal
                )
            }

            statistics.mostFrequentMood?.let { mood ->
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MindSpotColors.LightGray
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = mood.emoji,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "En sık hissettiğin: ${mood.displayName}",
                            fontSize = 14.sp,
                            color = MindSpotColors.DarkGray
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tek bir istatistik item'ı
 */
@Composable
private fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MindSpotColors.DarkGray
        )
    }
}

/**
 * Son kayıtlar bölümü
 */
@Composable
private fun RecentEntriesSection(entries: List<MoodEntry>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📝 Son Kayıtlar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (entries.isEmpty()) {
                Text(
                    text = "Henüz kayıt yok",
                    fontSize = 14.sp,
                    color = MindSpotColors.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                entries.forEach { entry ->
                    MoodEntryItem(entry = entry)
                    if (entry != entries.last()) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

/**
 * Tek bir ruh hali kayıt item'ı
 */
@Composable
private fun MoodEntryItem(entry: MoodEntry) {
    val moodType = MoodType.fromId(entry.moodId)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ruh hali emoji ve renk
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when (moodType) {
                        MoodType.GREAT -> MindSpotColors.MoodGreat
                        MoodType.GOOD -> MindSpotColors.MoodGood
                        MoodType.NORMAL -> MindSpotColors.MoodNormal
                        MoodType.BAD -> MindSpotColors.MoodBad
                        MoodType.VERY_BAD -> MindSpotColors.MoodVeryBad
                        null -> Color.Gray
                    }.copy(alpha = 0.2f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = moodType?.emoji ?: "❓",
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = moodType?.displayName ?: "Bilinmeyen",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MindSpotColors.DarkGray
            )

            Row {
                Text(
                    text = DateUtils.formatDate(entry.timestamp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = DateUtils.formatTime(entry.timestamp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                entry.optionalTag?.let { tag ->
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MindSpotColors.DeepBlue.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = tag,
                            fontSize = 10.sp,
                            color = MindSpotColors.DeepBlue
                        )
                    }
                }
            }
        }
    }
}

/**
 * Boş durum kartı
 */
@Composable
private fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MindSpotColors.LightGray
        )
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊",
                fontSize = 48.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Henüz Kayıt Yok",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MindSpotColors.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "İlk ruh hali kaydını yapmak için ana ekrana dön",
                fontSize = 14.sp,
                color = MindSpotColors.DarkGray,
                textAlign = TextAlign.Center
            )
        }
    }
}