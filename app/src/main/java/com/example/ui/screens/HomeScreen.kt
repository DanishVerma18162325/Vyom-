package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ScreenSearchDesktop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MessageEntity
import com.example.data.model.VyomAssistantState
import com.example.ui.components.MessageListView
import com.example.ui.components.VyomOrb
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    state: VyomAssistantState,
    messages: List<MessageEntity>,
    onSuggestionClick: (String) -> Unit,
    onSpeak: (String) -> Unit,
    onShare: (String) -> Unit,
    onRegenerate: () -> Unit,
    onSaveToMemory: (String) -> Unit,
    onExecuteAction: (toolName: String, payload: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (messages.isEmpty()) {
            // Minimalist ChatGPT/Gemini-style Hero Greeting
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))

                    // Minimalist Orb Beacon
                    VyomOrb(
                        state = state,
                        size = 110.dp,
                        modifier = Modifier.testTag("home_hero_orb")
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Greeting Typography
                    Text(
                        text = "What can I help you with?",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = VyomInkPrimary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "“Your Intelligence. Everywhere.”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            color = VyomInkMuted,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Suggestions Grid
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf(
                            Triple("Talk to Vyom", Icons.Default.Mic, "Start a live multimodal voice conversation with Vyom"),
                            Triple("Search the web", Icons.Default.Search, "Search latest news on AI breakthroughs"),
                            Triple("Summarize screen", Icons.Default.ScreenSearchDesktop, "Summarize active screen context"),
                            Triple("Create image", Icons.Default.Image, "Create an image of futuristic space habitat"),
                            Triple("Scan with camera", Icons.Default.CameraAlt, "Explain this real-world object using Vision"),
                            Triple("Study with me", Icons.Default.School, "Help me study physics concepts and equations"),
                            Triple("Set reminder", Icons.Default.Notifications, "Remind me to review my project tomorrow at 9 AM")
                        ).forEach { (label, icon, prompt) ->
                            SuggestionPill(
                                label = label,
                                icon = icon,
                                onClick = { onSuggestionClick(prompt) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        } else {
            // Conversational Stream View
            MessageListView(
                messages = messages,
                onSpeak = onSpeak,
                onShare = onShare,
                onRegenerate = onRegenerate,
                onSaveToMemory = onSaveToMemory,
                onExecuteAction = onExecuteAction,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SuggestionPill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, VyomGlassBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("suggestion_pill_${label.lowercase().replace(" ", "_")}")
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = VyomAtmosphereBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = VyomInkPrimary
            )
        }
    }
}
