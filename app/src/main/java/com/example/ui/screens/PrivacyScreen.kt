package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.ScreenSearchDesktop
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun PrivacyScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var onDevicePreference by remember { mutableStateOf(false) }
    var strictConfirmation by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VyomPureWhite)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("privacy_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = VyomInkPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "✦ VYOM PRIVACY CENTER",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = VyomInkPrimary
                    )
                )
                Text(
                    text = "Transparent Permissions & Zero-Silent-Listening",
                    fontSize = 12.sp,
                    color = VyomInkMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Privacy Guarantee Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFDCFCE7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Strict Privacy Architecture",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = VyomInkPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "VYOM never listens secretly in the background. All sensors activate only upon direct user request.",
                                fontSize = 12.sp,
                                color = VyomInkMuted,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "AI COMPUTATION CONTROLS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
            }

            item {
                PrivacyToggleCard(
                    title = "Prefer On-Device Processing",
                    subtitle = "Route queries through local intelligence whenever feasible.",
                    checked = onDevicePreference,
                    onCheckedChange = { onDevicePreference = it }
                )
            }

            item {
                PrivacyToggleCard(
                    title = "Consequential Action Approval",
                    subtitle = "Require biometric/visual preview before calls or messages.",
                    checked = strictConfirmation,
                    onCheckedChange = { strictConfirmation = it }
                )
            }

            item {
                Text(
                    text = "HARDWARE PERMISSION TRANSPARENCY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
            }

            item {
                PermissionInfoCard(
                    title = "Microphone (Audio Recognition)",
                    status = "Granted on user invocation",
                    description = "Used strictly when tapping Mic or inside VYOM Live mode.",
                    icon = Icons.Default.Mic
                )
            }

            item {
                PermissionInfoCard(
                    title = "Camera (Vision Intelligence)",
                    status = "Active during Vision screen only",
                    description = "Used to analyze diagrams, OCR documents, or real-world objects.",
                    icon = Icons.Default.CameraAlt
                )
            }

            item {
                PermissionInfoCard(
                    title = "Screen Context & Circle Search",
                    status = "Interactive Overlay",
                    description = "Extracts text and objects only in the explicitly highlighted bounding box.",
                    icon = Icons.Default.ScreenSearchDesktop
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEEF2FF))
                        .clickable {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Open Android System Permission Settings",
                        color = VyomAtmosphereBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyToggleCard(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = VyomInkPrimary)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = subtitle, fontSize = 12.sp, color = VyomInkMuted)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = VyomAtmosphereBlue
                )
            )
        }
    }
}

@Composable
fun PermissionInfoCard(
    title: String,
    status: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFD)),
        border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = VyomAtmosphereBlue, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = VyomInkPrimary)
                }
                Text(text = status, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                Text(text = description, fontSize = 11.sp, color = VyomInkMuted)
            }
        }
    }
}
