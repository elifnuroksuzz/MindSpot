package com.example.mindspotapp.presentation.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindspotapp.data.model.MoodEntry
import com.example.mindspotapp.data.model.MoodType
import com.example.mindspotapp.presentation.theme.MindSpotColors
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

/**
 * Ruh hali trend grafiği bileşeni
 * Son 7 günlük verileri çizgi grafiği olarak gösterir
 */
@Composable
fun MoodTrendChart(
    entries: List<MoodEntry>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "📈 Son 7 Gün Trend",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (entries.isEmpty()) {
                EmptyChartMessage()
            } else {
                val chartData = prepareChartData(entries)
                MoodLineChart(
                    data = chartData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }
    }
}

/**
 * Ruh hali dağılımı pasta grafik bileşeni
 */
@Composable
fun MoodDistributionChart(
    moodCounts: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🥧 Ruh Hali Dağılımı",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MindSpotColors.DarkGray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (moodCounts.isEmpty()) {
                EmptyChartMessage()
            } else {
                MoodDistributionBars(moodCounts = moodCounts)
            }
        }
    }
}

/**
 * Çizgi grafik çizimi
 */
@Composable
private fun MoodLineChart(
    data: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val padding = 40f

        val chartWidth = width - (padding * 2)
        val chartHeight = height - (padding * 2)

        // Y ekseni: 1-5 arası ruh hali değerleri
        val minValue = 1f
        val maxValue = 5f
        val valueRange = maxValue - minValue

        // Grid çizgileri
        drawGridLines(padding, chartWidth, chartHeight, minValue, maxValue)

        // Veri noktalarını hesapla
        val points = data.mapIndexed { index, point ->
            val x = padding + (index.toFloat() / (data.size - 1)) * chartWidth
            val y = padding + chartHeight - ((point.value - minValue) / valueRange) * chartHeight
            Offset(x, y)
        }

        // Çizgi çiz
        if (points.size > 1) {
            val path = Path()
            path.moveTo(points.first().x, points.first().y)

            for (i in 1 until points.size) {
                path.lineTo(points[i].x, points[i].y)
            }

            drawPath(
                path = path,
                color = MindSpotColors.EmeraldGreen,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
            )
        }

        // Noktaları çiz
        points.forEach { point ->
            drawCircle(
                color = MindSpotColors.EmeraldGreen,
                radius = 6.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = point
            )
        }
    }
}

/**
 * Grid çizgileri çizer
 */
private fun DrawScope.drawGridLines(
    padding: Float,
    chartWidth: Float,
    chartHeight: Float,
    minValue: Float,
    maxValue: Float
) {
    val valueRange = maxValue - minValue
    val gridColor = Color.Gray.copy(alpha = 0.3f)

    // Yatay grid çizgileri (ruh hali seviyeleri için)
    for (i in 1..5) {
        val y = padding + chartHeight - ((i - minValue) / valueRange) * chartHeight
        drawLine(
            color = gridColor,
            start = Offset(padding, y),
            end = Offset(padding + chartWidth, y),
            strokeWidth = 1.dp.toPx()
        )
    }
}

/**
 * Ruh hali dağılımı çubuk grafik
 */
@Composable
private fun MoodDistributionBars(moodCounts: Map<Int, Int>) {
    val totalCount = moodCounts.values.sum()
    if (totalCount == 0) return

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        (1..5).forEach { moodId ->
            val count = moodCounts[moodId] ?: 0
            val percentage = if (totalCount > 0) (count.toFloat() / totalCount) * 100 else 0f
            val moodType = MoodType.fromId(moodId)

            if (count > 0) {
                MoodDistributionBar(
                    moodType = moodType,
                    count = count,
                    percentage = percentage
                )
            }
        }
    }
}

/**
 * Tek bir ruh hali çubuğu
 */
@Composable
private fun MoodDistributionBar(
    moodType: MoodType?,
    count: Int,
    percentage: Float
) {
    if (moodType == null) return

    val barColor = when (moodType) {
        MoodType.GREAT -> MindSpotColors.MoodGreat
        MoodType.GOOD -> MindSpotColors.MoodGood
        MoodType.NORMAL -> MindSpotColors.MoodNormal
        MoodType.BAD -> MindSpotColors.MoodBad
        MoodType.VERY_BAD -> MindSpotColors.MoodVeryBad
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emoji ve isim
        Text(
            text = "${moodType.emoji} ${moodType.displayName}",
            fontSize = 14.sp,
            modifier = Modifier.width(100.dp)
        )

        // Çubuk
        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(percentage / 100f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(barColor)
            )
        }

        // Yüzde ve sayı
        Text(
            text = "${percentage.toInt()}% ($count)",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.width(60.dp),
            textAlign = TextAlign.End
        )
    }
}

/**
 * Boş grafik mesajı
 */
@Composable
private fun EmptyChartMessage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Grafik için yeterli veri yok\nDaha fazla kayıt yapın",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Grafik verisi için data sınıfları
 */
data class ChartPoint(
    val date: String,
    val value: Float
)

/**
 * Son 7 günlük veriyi grafik formatına çevirir
 */
private fun prepareChartData(entries: List<MoodEntry>): List<ChartPoint> {
    if (entries.isEmpty()) return emptyList()

    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    val calendar = Calendar.getInstance()

    // Son 7 günü al
    val last7Days = mutableListOf<String>()
    for (i in 6 downTo 0) {
        calendar.timeInMillis = System.currentTimeMillis() - (i * 24 * 60 * 60 * 1000L)
        last7Days.add(dateFormat.format(calendar.time))
    }

    // Her gün için ortalama ruh hali hesapla
    return last7Days.map { dateString ->
        calendar.time = dateFormat.parse(dateString) ?: Date()
        val dayStart = calendar.timeInMillis
        val dayEnd = dayStart + (24 * 60 * 60 * 1000L)

        val dayEntries = entries.filter { it.timestamp in dayStart..dayEnd }
        val averageMood = if (dayEntries.isNotEmpty()) {
            dayEntries.map { it.moodId }.average().toFloat()
        } else {
            0f // Veri yoksa 0
        }

        ChartPoint(dateString, averageMood)
    }.filter { it.value > 0 } // Sadece veri olan günleri göster
}