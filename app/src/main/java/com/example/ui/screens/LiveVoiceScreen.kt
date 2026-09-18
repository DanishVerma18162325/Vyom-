package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VyomAssistantState
import com.example.ui.components.VyomAtmosphericBackground
import com.example.ui.components.VyomOrb
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun LiveVoiceScreen(
    state: VyomAssistantState,
    audioRms: Float,
    isListening: Boolean,
    isSpeaking: Boolean,
    spokenTranscript: String,
    lastAiResponse: String,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    onStopSpeaking: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLanguage by remember { mutableStateOf("English") }

    VyomAtmosphericBackground(state = state, modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Branding + Language selector + Close
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_live_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Live Voice",
                        tint = VyomInkPrimary
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(4.dp)
                ) {
                    listOf("English", "हिंदी", "Hinglish").forEach { lang ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (selectedLanguage == lang) VyomPureWhite else Color.Transparent)
                                .clickable { selectedLanguage = lang }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = lang,
                                fontSize = 12.sp,
                                fontWeight = if (selectedLanguage == lang) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedLanguage == lang) VyomAtmosphereBlue else VyomInkMuted
                            )
                        }
                    }
                }
            }

            // Center: Massive Interactive VYOM Orb
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = when {
                        isSpeaking -> "Speaking…"
                        isListening -> "Listening to you…"
                        state == VyomAssistantState.THINKING -> "Thinking…"
                        else -> "Tap to speak with Vyom"
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = VyomInkPrimary
                    )
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Hero Animated Orb
                VyomOrb(
                    state = state,
                    audioRms = audioRms,
                    size = 220.dp,
                    modifier = Modifier.testTag("hero_vyom_orb")
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Real-time transcript preview
                if (spokenTranscript.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, VyomGlassBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "\"$spokenTranscript\"",
                            fontSize = 14.sp,
                            color = VyomInkPrimary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else if (lastAiResponse.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, VyomGlassBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = lastAiResponse.take(120) + if (lastAiResponse.length > 120) "…" else "",
                            fontSize = 13.sp,
                            color = VyomInkMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Bottom Controls: Mic Toggle / Interruption
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isSpeaking) {
                    // Barge-in / Interrupt button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFFEF4444))
                            .clickable { onStopSpeaking() }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Interrupt",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Interrupt & Speak",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    // Mic button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(if (isListening) Color(0xFFEF4444) else VyomAtmosphereBlue)
                            .clickable {
                                if (isListening) onStopListening() else onStartListening()
                            }
                            .testTag("toggle_voice_recognition_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Toggle Mic",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}
