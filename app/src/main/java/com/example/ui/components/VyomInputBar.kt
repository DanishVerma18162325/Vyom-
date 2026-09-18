package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun VyomInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: (String) -> Unit,
    onMicClick: () -> Unit,
    onCameraClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    onLiveVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Floating glass rounded bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 6.dp,
                    shape = RoundedCornerShape(28.dp),
                    spotColor = Color(0x180F172A)
                )
                .clip(RoundedCornerShape(28.dp))
                .background(VyomPureWhite)
                .border(
                    width = 1.dp,
                    color = VyomGlassBorder,
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // ＋ Attachment / Tool Picker Button
                IconButton(
                    onClick = onAttachmentClick,
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("attachment_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tools & Attachments",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Text Input Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp)
                        .heightIn(min = 36.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (text.isEmpty()) {
                        Text(
                            text = "Ask Vyom anything…",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = VyomInkMuted,
                                fontWeight = FontWeight.Normal
                            )
                        )
                    }

                    BasicTextField(
                        value = text,
                        onValueChange = onTextChange,
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = VyomInkPrimary,
                            fontWeight = FontWeight.Normal
                        ),
                        cursorBrush = SolidColor(VyomAtmosphereBlue),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (text.isNotBlank()) {
                                    onSend(text.trim())
                                }
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("chat_input_field")
                    )
                }

                // Camera button
                IconButton(
                    onClick = onCameraClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("camera_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Camera Vision",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Mic button
                IconButton(
                    onClick = onMicClick,
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("microphone_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Input",
                        tint = VyomAtmosphereBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Dynamic Send or Live Voice Orb trigger
                AnimatedVisibility(
                    visible = text.isNotBlank(),
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut()
                ) {
                    IconButton(
                        onClick = {
                            if (text.isNotBlank()) {
                                onSend(text.trim())
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(VyomAtmosphereBlue, VyomAtmospherePurple)
                                )
                            )
                            .testTag("send_message_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (text.isBlank()) {
                    // Live Voice / ✦ AI Mode quick button
                    IconButton(
                        onClick = onLiveVoiceClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                            .testTag("live_voice_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Vyom Live Mode",
                            tint = VyomAtmospherePurple,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}
