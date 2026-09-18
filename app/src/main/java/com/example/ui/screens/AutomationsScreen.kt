package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AutomationRuleEntity
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun AutomationsScreen(
    automations: List<AutomationRuleEntity>,
    onToggleAutomation: (AutomationRuleEntity) -> Unit,
    onRunBriefing: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                modifier = Modifier.testTag("automations_back_button")
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
                    text = "✦ VYOM AUTOMATIONS",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = VyomInkPrimary
                    )
                )
                Text(
                    text = "Contextual triggers & Daily Intelligence",
                    fontSize = 12.sp,
                    color = VyomInkMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Daily Briefing Hero Card
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFEF3C7)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Good Morning with VYOM",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = VyomInkPrimary
                                )
                                Text(
                                    text = "Weather • Calendar • Priorities • News",
                                    fontSize = 12.sp,
                                    color = VyomInkMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Synthesizes your upcoming meetings, prioritized agenda, local forecast, and top headlines into one calm audio briefing.",
                            fontSize = 13.sp,
                            color = VyomInkMuted,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(VyomAtmosphereBlue)
                                .clickable { onRunBriefing() }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                                .testTag("run_daily_briefing_button")
                        ) {
                            Text(
                                text = "Preview Briefing Now",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "ACTIVE AUTOMATION RULES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
            }

            // Built-in list of automations
            val defaultRules = if (automations.isNotEmpty()) automations else listOf(
                AutomationRuleEntity(
                    id = 1,
                    title = "Morning Briefing at 7:00 AM",
                    triggerType = "Every day at 7:00 AM",
                    actionType = "Audio Synthesis",
                    actionPayload = "Deliver weather, news, schedule",
                    isEnabled = true
                ),
                AutomationRuleEntity(
                    id = 2,
                    title = "Headphones Music Trigger",
                    triggerType = "When Bluetooth Headset connects",
                    actionType = "App Launch",
                    actionPayload = "Open preferred streaming media",
                    isEnabled = true
                ),
                AutomationRuleEntity(
                    id = 3,
                    title = "Weekly Sunday Planning",
                    triggerType = "Every Sunday at 6:00 PM",
                    actionType = "Digest Summary",
                    actionPayload = "Summarize upcoming weekly goals",
                    isEnabled = false
                )
            )

            items(defaultRules) { rule ->
                AutomationCard(
                    rule = rule,
                    onToggle = { onToggleAutomation(rule.copy(isEnabled = !rule.isEnabled)) }
                )
            }
        }
    }
}

@Composable
fun AutomationCard(
    rule: AutomationRuleEntity,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEEF2FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (rule.title.contains("Headphone")) Icons.Default.Headphones else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = VyomAtmosphereBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = rule.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = VyomInkPrimary
                    )
                    Text(
                        text = rule.triggerType,
                        fontSize = 12.sp,
                        color = VyomInkMuted
                    )
                }
            }

            Switch(
                checked = rule.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = VyomAtmosphereBlue
                )
            )
        }
    }
}
