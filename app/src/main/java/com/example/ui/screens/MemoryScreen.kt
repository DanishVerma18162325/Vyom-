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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MemoryEntity
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@Composable
fun MemoryScreen(
    memories: List<MemoryEntity>,
    onAddMemory: (category: String, content: String) -> Unit,
    onDeleteMemory: (Long) -> Unit,
    onClearAll: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newMemoryText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Preferences") }

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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("memory_back_button")
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
                        text = "✦ PERSONAL MEMORY",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = VyomInkPrimary
                        )
                    )
                    Text(
                        text = "Encrypted On-Device Context & Preferences",
                        fontSize = 12.sp,
                        color = VyomInkMuted
                    )
                }
            }

            if (memories.isNotEmpty()) {
                IconButton(
                    onClick = onClearAll,
                    modifier = Modifier.testTag("clear_all_memory_button")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Clear All Memories",
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input Card to Add Memory
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Teach VYOM a new preference or rule",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = VyomInkPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                BasicTextField(
                    value = newMemoryText,
                    onValueChange = { newMemoryText = it },
                    textStyle = TextStyle(fontSize = 14.sp, color = VyomInkPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VyomPureWhite)
                        .border(1.dp, VyomGlassBorder, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                        .testTag("new_memory_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Category selector
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Preferences", "Projects", "Instructions").forEach { cat ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selectedCategory == cat) Color(0xFFEEF2FF) else Color.Transparent)
                                    .clickable { selectedCategory = cat }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedCategory == cat) VyomAtmosphereBlue else VyomInkMuted
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(VyomAtmosphereBlue)
                            .clickable {
                                if (newMemoryText.isNotBlank()) {
                                    onAddMemory(selectedCategory, newMemoryText.trim())
                                    newMemoryText = ""
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("save_memory_rule_button")
                    ) {
                        Text(
                            text = "Add Rule",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "STORED PREFERENCES (${memories.size})",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = VyomInkMuted,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Display sample default items if empty
        val displayList = if (memories.isNotEmpty()) memories else listOf(
            MemoryEntity(
                id = 1,
                category = "Preferences",
                content = "Always prefer concise Markdown with clean bullet points."
            ),
            MemoryEntity(
                id = 2,
                category = "Instructions",
                content = "Support multilingual Hindi/Hinglish translations seamlessly."
            ),
            MemoryEntity(
                id = 3,
                category = "Projects",
                content = "Currently developing VYOM next-generation multimodal personal AI layer."
            )
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(displayList) { mem ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFD)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = mem.category,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = VyomAtmospherePurple
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = mem.content,
                                fontSize = 13.sp,
                                color = VyomInkPrimary,
                                lineHeight = 18.sp
                            )
                        }

                        IconButton(
                            onClick = { onDeleteMemory(mem.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Memory",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
