package com.example.mindspotapp.presentation.screens.settings

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mindspotapp.presentation.theme.*
import com.example.mindspotapp.utils.Constants
import com.example.mindspotapp.utils.PreferencesHelper

/**
 * Ayarlar ve kişiselleştirme ekranı
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val preferencesHelper = remember { PreferencesHelper(context) }

    // State'ler
    var selectedTheme by remember {
        mutableStateOf(
            AppTheme.values().find {
                it.name == preferencesHelper.getSelectedTheme()
            } ?: AppTheme.MINDSPOT_DEFAULT
        )
    }
    var isDarkMode by remember { mutableStateOf(preferencesHelper.isDarkModeEnabled()) }
    var customTags by remember { mutableStateOf(preferencesHelper.getCustomTags().toList()) }
    var showAddTagDialog by remember { mutableStateOf(false) }
    var newTagText by remember { mutableStateOf("") }

    // Activity'yi yeniden başlatma fonksiyonu
    val activity = LocalContext.current as? ComponentActivity

    fun restartActivity() {
        activity?.let {
            it.finish()
            it.startActivity(it.intent)
        }
    }

    // Add tag dialog
    if (showAddTagDialog) {
        AlertDialog(
            onDismissRequest = { showAddTagDialog = false },
            title = { Text("Yeni Etiket Ekle") },
            text = {
                OutlinedTextField(
                    value = newTagText,
                    onValueChange = { newTagText = it },
                    label = { Text("Etiket adı") },
                    placeholder = { Text("Örn: Yoga, Müzik") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTagText.isNotBlank() && newTagText.length <= 15) {
                            preferencesHelper.addCustomTag(newTagText.trim())
                            customTags = preferencesHelper.getCustomTags().toList()
                            newTagText = ""
                            showAddTagDialog = false
                        }
                    }
                ) {
                    Text("Ekle")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTagDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Geri",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Text(
                    text = "Ayarlar",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        // Tema seçimi
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎨 Tema Seçimi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(AppTheme.values()) { theme ->
                            ThemeCard(
                                theme = theme,
                                isSelected = selectedTheme == theme,
                                onClick = {
                                    selectedTheme = theme
                                    preferencesHelper.setSelectedTheme(theme.name)
                                    // Tema değişikliği için activity'yi yeniden başlat
                                    restartActivity()
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dark mode toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🌙 Karanlık Mod",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { enabled ->
                                isDarkMode = enabled
                                preferencesHelper.setDarkModeEnabled(enabled)
                                // Karanlık mod değişikliği için activity'yi yeniden başlat
                                restartActivity()
                            }
                        )
                    }
                }
            }
        }

        // Özel etiketler
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🏷️ Özel Etiketler",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = { showAddTagDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Etiket ekle",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (customTags.isEmpty()) {
                        Text(
                            text = "Henüz özel etiket eklenmemiş. + butonuna basarak ekleyebilirsin.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            items(customTags) { tag ->
                                CustomTagChip(
                                    tag = tag,
                                    onRemove = {
                                        preferencesHelper.removeCustomTag(tag)
                                        customTags = preferencesHelper.getCustomTags().toList()
                                    }
                                )
                            }
                        }
                    }

                    Text(
                        text = "İpucu: Ruh hali kaydederken daha spesifik etiketler kullanarak hangi aktivitelerin seni nasıl etkilediğini keşfedebilirsin.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        // Bildirim ayarları
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🔔 Bildirim Ayarları",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Günlük Hatırlatıcılar",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Ruh hali kaydı için günlük bildirimler",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Switch(
                            checked = preferencesHelper.isNotificationEnabled(),
                            onCheckedChange = { enabled ->
                                preferencesHelper.setNotificationEnabled(enabled)
                            }
                        )
                    }
                }
            }
        }

        // Uygulama bilgileri
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "ℹ️ Uygulama Bilgileri",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Versiyon",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "1.0.0",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "MindSpot - Sade Duygu Takipçisi\nKendini gözlemlemenin en hızlı yolu.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Alt boşluk
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/**
 * Tema kartı bileşeni
 */
@Composable
private fun ThemeCard(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected)
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = theme.emoji,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = theme.displayName,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Özel etiket chip'i
 */
@Composable
private fun CustomTagChip(
    tag: String,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = tag,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.width(4.dp))

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Etiket sil",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onRemove() }
            )
        }
    }
}