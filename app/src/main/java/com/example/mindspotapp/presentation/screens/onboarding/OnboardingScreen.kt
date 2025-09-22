package com.example.mindspotapp.presentation.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import com.example.mindspotapp.presentation.navigation.Screen
import com.example.mindspotapp.presentation.theme.MindSpotColors
import kotlinx.coroutines.launch

/**
 * Onboarding ekranı - İlk kullanım tutorial'ı
 * 3 sayfalık basit tanıtım
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    onOnboardingComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    // Onboarding sayfaları
    val onboardingPages = listOf(
        OnboardingPage(
            emoji = "🎯",
            title = "MindSpot'a Hoş Geldin",
            description = "Duygularını takip etmenin en hızlı ve basit yolu. Karmaşık günlük tutma yerine sadece tek dokunuşla ruh halini kaydet.",
            backgroundColor = MindSpotColors.EmeraldGreen
        ),
        OnboardingPage(
            emoji = "⚡",
            title = "Sürtünmesiz Kayıt",
            description = "5 saniyede ruh halini seç, opsiyonel etiket ekle ve kaydet. Bu kadar basit! Günlük tutma alışkanlığın yoksa bile kullanabilirsin.",
            backgroundColor = MindSpotColors.DeepBlue
        ),
        OnboardingPage(
            emoji = "📊",
            title = "Duygusal Farkındalık",
            description = "Zaman içindeki ruh hali değişimlerini görselleştir. Kendini daha iyi tanımaya başla ve örüntüleri keşfet.",
            backgroundColor = MindSpotColors.MoodNormal
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Progress indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MindSpotColors.EmeraldGreen
                            else Color.Gray.copy(alpha = 0.3f)
                        )
                )
                if (index < 2) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Pager content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(onboardingPages[page])
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Skip button
            if (pagerState.currentPage < 2) {
                TextButton(
                    onClick = {
                        onOnboardingComplete()
                        navController.navigate(Screen.MoodEntry.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                ) {
                    Text(
                        text = "Geç",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(64.dp))
            }

            // Next/Finish button
            Button(
                onClick = {
                    if (pagerState.currentPage < 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onOnboardingComplete()
                        navController.navigate(Screen.NotificationPermission.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindSpotColors.EmeraldGreen
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = if (pagerState.currentPage < 2) "İleri" else "Başla",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Tek bir onboarding sayfası içeriği
 */
@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ana emoji/ikon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(page.backgroundColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = page.emoji,
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Başlık
        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MindSpotColors.DarkGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Açıklama
        Text(
            text = page.description,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

/**
 * Onboarding sayfa veri modeli
 */
data class OnboardingPage(
    val emoji: String,
    val title: String,
    val description: String,
    val backgroundColor: Color
)