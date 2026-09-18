package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ConversationEntity
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomDivider
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun SidebarDrawer(
    conversations: List<ConversationEntity>,
    currentConversationId: String,
    onSelectConversation: (String) -> Unit,
    onNewChat: () -> Unit,
    onDeleteConversation: (String) -> Unit,
    onOpenLiveVoice: () -> Unit,
    onOpenVision: () -> Unit,
    onOpenStudy: () -> Unit,
    onOpenAutomations: () -> Unit,
    onOpenMemory: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = VyomPureWhite,
        modifier = modifier
            .fillMaxHeight()
            .width(310.dp)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Header: ✦ VYOM Brand
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEF2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✦",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = VyomAtmospherePurple
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "VYOM",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = VyomInkPrimary
                        )
                    )
                }

                // New Chat Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF1F5F9))
                        .clickable { onNewChat() }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                        .testTag("drawer_new_chat_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = VyomAtmosphereBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "New Conversation",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = VyomInkPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Core Feature Shortcuts
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    DrawerNavRow(
                        label = "VYOM Live Voice",
                        icon = Icons.Default.AutoAwesome,
                        iconTint = VyomAtmospherePurple,
                        onClick = onOpenLiveVoice
                    )
                    DrawerNavRow(
                        label = "Vision Camera",
                        icon = Icons.Default.CameraAlt,
                        iconTint = VyomAtmosphereBlue,
                        onClick = onOpenVision
                    )
                    DrawerNavRow(
                        label = "Study Assistant",
                        icon = Icons.Default.School,
                        iconTint = Color(0xFF06B6D4),
                        onClick = onOpenStudy
                    )
                    DrawerNavRow(
                        label = "Automations & Briefing",
                        icon = Icons.Default.Bolt,
                        iconTint = Color(0xFFF59E0B),
                        onClick = onOpenAutomations
                    )
                    DrawerNavRow(
                        label = "Personal Memory Vault",
                        icon = Icons.Default.Bookmark,
                        iconTint = Color(0xFF10B981),
                        onClick = onOpenMemory
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = VyomDivider, thickness = 1.dp)
                Spacer(modifier = Modifier.height(12.dp))

                // Conversation History List
                Text(
                    text = "CONVERSATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(conversations, key = { it.id }) { conv ->
                        val isSelected = conv.id == currentConversationId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFEEF2FF) else Color.Transparent)
                                .clickable { onSelectConversation(conv.id) }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                                    contentDescription = null,
                                    tint = if (isSelected) VyomAtmosphereBlue else VyomInkMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = conv.title,
                                    fontSize = 13.sp,
                                    color = if (isSelected) VyomAtmosphereBlue else VyomInkPrimary,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            IconButton(
                                onClick = { onDeleteConversation(conv.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFCBD5E1),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom: Privacy Center & System Status
            Column {
                Divider(color = VyomDivider, thickness = 1.dp)
                Spacer(modifier = Modifier.height(10.dp))

                DrawerNavRow(
                    label = "Privacy Center",
                    icon = Icons.Default.Shield,
                    iconTint = Color(0xFF64748B),
                    onClick = onOpenPrivacy
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Account status pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(VyomAtmosphereBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "V",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "VYOM Core Intelligence",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VyomInkPrimary
                            )
                            Text(
                                text = "Local & Cloud Neural Engine",
                                fontSize = 10.sp,
                                color = VyomInkMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerNavRow(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = VyomInkPrimary
        )
    }
}
