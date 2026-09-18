package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.MessageEntity
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite
import com.example.ui.theme.VyomUserBubble

@Composable
fun MessageListView(
    messages: List<MessageEntity>,
    onSpeak: (String) -> Unit,
    onShare: (String) -> Unit,
    onRegenerate: () -> Unit,
    onSaveToMemory: (String) -> Unit,
    onExecuteAction: (toolName: String, payload: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(messages, key = { it.id }) { msg ->
            if (msg.isUser) {
                UserMessageCard(text = msg.text, imageUrl = msg.imageUrl)
            } else {
                VyomMessageView(
                    message = msg,
                    onCopy = {
                        clipboardManager.setText(AnnotatedString(msg.text))
                    },
                    onSpeak = { onSpeak(msg.text) },
                    onShare = { onShare(msg.text) },
                    onRegenerate = onRegenerate,
                    onSaveToMemory = { onSaveToMemory(msg.text) },
                    onExecuteAction = onExecuteAction
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun UserMessageCard(
    text: String,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Attached Image",
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, VyomGlassBorder, RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .background(VyomUserBubble)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp,
                        color = VyomInkPrimary,
                        lineHeight = 22.sp
                    )
                )
            }
        }
    }
}

@Composable
fun VyomMessageView(
    message: MessageEntity,
    onCopy: () -> Unit,
    onSpeak: () -> Unit,
    onShare: () -> Unit,
    onRegenerate: () -> Unit,
    onSaveToMemory: () -> Unit,
    onExecuteAction: (toolName: String, payload: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf<Boolean?>(null) }
    var copiedNotice by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        // Subtle Brand Accent header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✦",
                    fontSize = 10.sp,
                    color = VyomAtmospherePurple,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "VYOM",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = VyomInkMuted,
                    letterSpacing = 0.8.sp
                )
            )
        }

        // Clean response text - no unnecessary bubble container (ChatGPT-style)
        FormattedMarkdownText(text = message.text)

        // Interactive Tool Card (Confirmation / Action button)
        if (message.toolName != null && message.toolResult != null) {
            Spacer(modifier = Modifier.height(10.dp))
            ToolActionCard(
                toolName = message.toolName,
                payload = message.toolResult,
                onExecute = { onExecuteAction(message.toolName, message.toolResult) }
            )
        }

        // Sources citation pill row if available
        if (!message.sources.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Sources:",
                    fontSize = 11.sp,
                    color = VyomInkMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = message.sources,
                    fontSize = 11.sp,
                    color = VyomAtmosphereBlue
                )
            }
        }

        // Actions Row (Copy, Read Aloud, Share, Regenerate, Thumbs, Save)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = {
                    onCopy()
                    copiedNotice = true
                },
                modifier = Modifier.size(32.dp).testTag("copy_button")
            ) {
                Icon(
                    imageVector = if (copiedNotice) Icons.Default.Check else Icons.Outlined.ContentCopy,
                    contentDescription = "Copy text",
                    tint = if (copiedNotice) Color(0xFF10B981) else Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = onSpeak,
                modifier = Modifier.size(32.dp).testTag("speak_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.VolumeUp,
                    contentDescription = "Read Aloud",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = onShare,
                modifier = Modifier.size(32.dp).testTag("share_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = onRegenerate,
                modifier = Modifier.size(32.dp).testTag("regenerate_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = "Regenerate",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = onSaveToMemory,
                modifier = Modifier.size(32.dp).testTag("save_memory_button")
            ) {
                Icon(
                    imageVector = Icons.Outlined.BookmarkBorder,
                    contentDescription = "Save to Personal Memory",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = { isLiked = true },
                modifier = Modifier.size(32.dp).testTag("thumbs_up_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Good response",
                    tint = if (isLiked == true) VyomAtmosphereBlue else Color(0xFFCBD5E1),
                    modifier = Modifier.size(14.dp)
                )
            }

            IconButton(
                onClick = { isLiked = false },
                modifier = Modifier.size(32.dp).testTag("thumbs_down_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbDown,
                    contentDescription = "Bad response",
                    tint = if (isLiked == false) Color(0xFFEF4444) else Color(0xFFCBD5E1),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun ToolActionCard(
    toolName: String,
    payload: String,
    onExecute: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (toolName) {
                            "callContact" -> Icons.Default.Phone
                            "setAlarm", "setTimer" -> Icons.Default.Alarm
                            else -> Icons.Default.Check
                        },
                        contentDescription = null,
                        tint = VyomAtmosphereBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = when (toolName) {
                            "callContact" -> "Confirm Phone Call"
                            "sendMessage" -> "Confirm Message"
                            "setAlarm" -> "Activate Alarm"
                            "setTimer" -> "Start Timer"
                            else -> "Action Ready"
                        },
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = VyomInkPrimary
                    )
                    Text(
                        text = payload,
                        fontSize = 12.sp,
                        color = VyomInkMuted
                    )
                }
            }

            Button(
                onClick = onExecute,
                colors = ButtonDefaults.buttonColors(containerColor = VyomAtmosphereBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = when (toolName) {
                        "callContact" -> "Call"
                        "sendMessage" -> "Send"
                        "setAlarm" -> "Set"
                        else -> "Execute"
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun FormattedMarkdownText(
    text: String,
    modifier: Modifier = Modifier
) {
    // Parse text paragraphs, bold headers, and code block formatting cleanly
    val paragraphs = text.split("\n\n")

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (para in paragraphs) {
            val trimmed = para.trim()
            if (trimmed.isEmpty()) continue

            if (trimmed.startsWith("```") && trimmed.endsWith("```")) {
                // Code block
                val code = trimmed.removeSurrounding("```").trim()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A))
                        .padding(12.dp)
                ) {
                    Text(
                        text = code,
                        color = Color(0xFF38BDF8),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            } else if (trimmed.startsWith("### ")) {
                // Heading 3
                Text(
                    text = trimmed.removePrefix("### "),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = VyomInkPrimary
                    )
                )
            } else {
                // Regular markdown paragraph
                val clean = trimmed.replace("**", "").replace("*", "")
                Text(
                    text = clean,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 15.sp,
                        lineHeight = 23.sp,
                        color = VyomInkPrimary
                    )
                )
            }
        }
    }
}
