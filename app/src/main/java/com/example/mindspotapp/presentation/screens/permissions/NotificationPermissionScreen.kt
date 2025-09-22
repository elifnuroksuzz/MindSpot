package com.example.mindspotapp.presentation.screens.permissions

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mindspotapp.presentation.navigation.Screen
import com.example.mindspotapp.presentation.theme.MindSpotColors
import com.example.mindspotapp.utils.NotificationHelper

/**
 * Bildirim izni isteme ekranı
 * Onboarding sonrası gösterilir
 */
@Composable
fun NotificationPermissionScreen(
    navController: NavController,
    onPermissionHandled: () -> Unit
) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }

    // Android 13+ için bildirim izni launcher'ı
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // İzin verilse de verilmese de ana ekrana geç
        onPermissionHandled()
        navController.navigate(Screen.MoodEntry.route) {
            popUpTo(Screen.NotificationPermission.route) { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // İkon
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MindSpotColors.EmeraldGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🔔",
                fontSize = 48.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Başlık
        Text(
            text = "Günlük Hatırlatıcılar",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MindSpotColors.DarkGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Açıklama
        Text(
            text = "Ruh hali kaydı yapmayı unutmamanız için günde bir kez nazik hatırlatıcı gönderebiliriz. İstediğiniz zaman kapatabilirsiniz.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Faydalar listesi
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MindSpotColors.LightGray
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                BenefitItem("📱", "Günlük hatırlatıcılar")
                Spacer(modifier = Modifier.height(8.dp))
                BenefitItem("✨", "Motivasyon mesajları")
                Spacer(modifier = Modifier.height(8.dp))
                BenefitItem("📊", "Haftalık özetler")
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Butonlar
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // İzin ver butonu
            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // Android 12 ve altında otomatik izin var
                        onPermissionHandled()
                        navController.navigate(Screen.MoodEntry.route) {
                            popUpTo(Screen.NotificationPermission.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MindSpotColors.EmeraldGreen
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bildirimlere İzin Ver",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Şimdilik geç butonu
            TextButton(
                onClick = {
                    onPermissionHandled()
                    navController.navigate(Screen.MoodEntry.route) {
                        popUpTo(Screen.NotificationPermission.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Şimdilik Geç",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

/**
 * Fayda listesi öğesi
 */
@Composable
private fun BenefitItem(
    icon: String,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 20.sp,
            modifier = Modifier.padding(end = 12.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = MindSpotColors.DarkGray
        )
    }
}