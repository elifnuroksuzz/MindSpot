package com.example.mindspotapp.presentation.screens.mood_entry

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mindspotapp.data.model.MoodType
import com.example.mindspotapp.presentation.navigation.Screen
import com.example.mindspotapp.presentation.theme.MindSpotColors
import com.example.mindspotapp.utils.Constants
import com.example.mindspotapp.utils.NotificationHelper
import com.example.mindspotapp.utils.HapticFeedbackHelper
import com.example.mindspotapp.utils.PreferencesHelper

/**
 * Ana ruh hali kayıt ekranı
 * Kullanıcının günlük ruh halini kaydetmesini sağlar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodEntryScreen(
    navController: NavController,
    viewModel: MoodEntryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }
    var selectedTag by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Haptic feedback helper - direkt burada oluştur
    val hapticFeedback: HapticFeedbackHelper = remember { HapticFeedbackHelper(context) }

    // Animasyon state'leri
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var isContentVisible by remember { mutableStateOf(false) }

    // İçerik animasyonu için LaunchedEffect
    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    // Etiketleri dinamik olarak al (default + custom)
    val preferencesHelper: PreferencesHelper = remember { PreferencesHelper(context) }
    var tagsKey by remember { mutableStateOf(0) } // Yenileme için key
    val defaultTags: List<String> = Constants.DEFAULT_TAGS.take(6)
    val customTags: List<String> = remember(tagsKey) { preferencesHelper.getCustomTags().toList() }
    val allTags: List<String> = remember(tagsKey) { (defaultTags + customTags).distinct().take(10) } // Maksimum 10 etiket

    // Navigation'dan dönüldüğünde etiketleri güncelle
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            if (destination.route == Screen.MoodEntry.route) {
                tagsKey++ // Etiketleri yenile
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    // Kayıt başarılı olduğunda animasyon ve haptic feedback
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            // Başarı haptic feedback'i
            hapticFeedback.successPattern()

            // Başarı animasyonu
            showSuccessAnimation = true

            // 2 saniye sonra state'leri sıfırla
            kotlinx.coroutines.delay(2000)
            selectedMood = null
            selectedTag = null
            showSuccessAnimation = false
            viewModel.resetUiState()
        }
    }

    // Scroll state'i oluştur
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Scroll özelliği eklendi
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Başlık Alanı
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(48.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "MindSpot",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MindSpotColors.EmeraldGreen
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Bugün nasıl hissediyorsun?",
                        fontSize = 18.sp,
                        color = MindSpotColors.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }

                IconButton(
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ayarlar",
                        tint = MindSpotColors.DarkGray
                    )
                }
            }
        }

        // Ruh Hali Seçenekleri
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ruh Halini Seç",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MindSpotColors.DarkGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Ruh hali seçenekleri
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MoodType.getAllMoods().forEachIndexed { index, moodType ->
                        val alpha by animateFloatAsState(
                            targetValue = if (isContentVisible) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = index * 100
                            ),
                            label = "mood_alpha"
                        )

                        val scale by animateFloatAsState(
                            targetValue = if (isContentVisible) 1f else 0f,
                            animationSpec = tween(
                                durationMillis = 300,
                                delayMillis = index * 100
                            ),
                            label = "mood_scale"
                        )

                        Box(
                            modifier = Modifier
                                .alpha(alpha)
                                .scale(scale)
                        ) {
                            MoodItem(
                                moodType = moodType,
                                isSelected = selectedMood == moodType,
                                onClick = {
                                    selectedMood = moodType
                                },
                                hapticFeedback = hapticFeedback
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Opsiyonel Etiketler
        if (selectedMood != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MindSpotColors.LightGray
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Neden böyle hissediyorsun? (Opsiyonel)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MindSpotColors.DarkGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Etiket seçenekleri
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            count = allTags.size
                        ) { index ->
                            val tag = allTags[index]
                            TagChip(
                                tag = tag,
                                isSelected = selectedTag == tag,
                                onClick = {
                                    selectedTag = if (selectedTag == tag) null else tag
                                },
                                hapticFeedback = hapticFeedback
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Kaydet Butonu
        if (selectedMood != null) {
            val alpha by animateFloatAsState(
                targetValue = if (selectedMood != null) 1f else 0f,
                animationSpec = tween(durationMillis = 300, delayMillis = 200),
                label = "button_alpha"
            )

            val scale by animateFloatAsState(
                targetValue = if (selectedMood != null) 1f else 0f,
                animationSpec = tween(durationMillis = 300, delayMillis = 200),
                label = "button_scale"
            )

            val loadingScale by animateFloatAsState(
                targetValue = if (!uiState.isLoading) 1f else 0.95f,
                animationSpec = tween(200),
                label = "button_loading_scale"
            )

            Box(
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale)
            ) {
                Button(
                    onClick = {
                        hapticFeedback.mediumImpact()
                        viewModel.saveMoodEntry(selectedMood!!.id, selectedTag)
                    },
                    enabled = !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MindSpotColors.EmeraldGreen
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(loadingScale),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Kaydet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Özet ekranına git butonu
        TextButton(
            onClick = {
                navController.navigate(Screen.Summary.route)
            }
        ) {
            Text(
                text = "Ruh Hali Geçmişini Gör",
                color = MindSpotColors.DeepBlue,
                fontSize = 16.sp
            )
        }

        // Success message with animation
        if (showSuccessAnimation) {
            val alpha by animateFloatAsState(
                targetValue = if (showSuccessAnimation) 1f else 0f,
                animationSpec = tween(300),
                label = "success_alpha"
            )

            val scale by animateFloatAsState(
                targetValue = if (showSuccessAnimation) 1f else 0f,
                animationSpec = tween(300),
                label = "success_scale"
            )

            Box(
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MindSpotColors.EmeraldGreen.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✅",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Ruh halin başarıyla kaydedildi!",
                            color = MindSpotColors.EmeraldGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Error message with animation
        uiState.errorMessage?.let { error ->
            val alpha by animateFloatAsState(
                targetValue = if (uiState.errorMessage != null) 1f else 0f,
                animationSpec = tween(300),
                label = "error_alpha"
            )

            val scale by animateFloatAsState(
                targetValue = if (uiState.errorMessage != null) 1f else 0f,
                animationSpec = tween(300),
                label = "error_scale"
            )

            Box(
                modifier = Modifier
                    .alpha(alpha)
                    .scale(scale)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚠️",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = error,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tek bir ruh hali seçeneği - animasyonlar ve haptic feedback ile
 */
@Composable
private fun MoodItem(
    moodType: MoodType,
    isSelected: Boolean,
    onClick: () -> Unit,
    hapticFeedback: HapticFeedbackHelper
) {
    val backgroundColor = when {
        isSelected -> when (moodType) {
            MoodType.GREAT -> MindSpotColors.MoodGreat
            MoodType.GOOD -> MindSpotColors.MoodGood
            MoodType.NORMAL -> MindSpotColors.MoodNormal
            MoodType.BAD -> MindSpotColors.MoodBad
            MoodType.VERY_BAD -> MindSpotColors.MoodVeryBad
        }.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val borderColor = when {
        isSelected -> when (moodType) {
            MoodType.GREAT -> MindSpotColors.MoodGreat
            MoodType.GOOD -> MindSpotColors.MoodGood
            MoodType.NORMAL -> MindSpotColors.MoodNormal
            MoodType.BAD -> MindSpotColors.MoodBad
            MoodType.VERY_BAD -> MindSpotColors.MoodVeryBad
        }
        else -> Color.Gray.copy(alpha = 0.3f)
    }

    // Seçim animasyonu
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(300),
        label = "mood_selection_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .animateContentSize(
                animationSpec = tween(300)
            )
            .clickable {
                hapticFeedback.selectionFeedback()
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 3.dp else 1.dp,
            color = borderColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = moodType.emoji,
                fontSize = if (isSelected) 28.sp else 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )

            Text(
                text = moodType.displayName,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MindSpotColors.DarkGray
            )
        }
    }
}

/**
 * Etiket chip bileşeni - haptic feedback ve animasyonlar ile
 */
@Composable
private fun TagChip(
    tag: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    hapticFeedback: HapticFeedbackHelper
) {
    val backgroundColor = if (isSelected) {
        MindSpotColors.DeepBlue
    } else {
        Color.Gray.copy(alpha = 0.2f)
    }

    val textColor = if (isSelected) {
        Color.White
    } else {
        MindSpotColors.DarkGray
    }

    // Seçim animasyonu
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = tween(200),
        label = "tag_selection_scale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable {
                hapticFeedback.lightImpact()
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = tag,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}