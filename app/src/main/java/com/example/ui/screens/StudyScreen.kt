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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudyScreen(
    onLaunchStudyTopic: (prompt: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedSubject by remember { mutableStateOf("Physics") }

    val subjects = listOf(
        "Mathematics", "Physics", "Chemistry", "Biology",
        "Computer Science", "History", "Geography", "English", "Hindi"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VyomPureWhite)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        // Top Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("study_back_button")
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
                    text = "✦ VYOM STUDY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = VyomInkPrimary
                    )
                )
                Text(
                    text = "Interactive Multimodal Learning & Exam Prep",
                    fontSize = 12.sp,
                    color = VyomInkMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Subject chips
            item {
                Text(
                    text = "SELECT DISCIPLINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subjects.forEach { subject ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (selectedSubject == subject) Color(0xFFEEF2FF) else Color(0xFFF8FAFC))
                                .border(
                                    1.dp,
                                    if (selectedSubject == subject) VyomAtmosphereBlue else VyomGlassBorder,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable { selectedSubject = subject }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = subject,
                                fontSize = 13.sp,
                                fontWeight = if (selectedSubject == subject) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedSubject == subject) VyomAtmosphereBlue else VyomInkPrimary
                            )
                        }
                    }
                }
            }

            // Study Actions / Modules
            item {
                Text(
                    text = "LEARNING MODES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
            }

            item {
                StudyActionCard(
                    title = "Step-by-Step Problem Solver",
                    subtitle = "Break down difficult $selectedSubject formulas and homework questions",
                    icon = Icons.Default.School,
                    onClick = {
                        onLaunchStudyTopic("Help me understand $selectedSubject: Provide a step-by-step conceptual derivation and solved example problem.")
                    }
                )
            }

            item {
                StudyActionCard(
                    title = "Generate Adaptive Quiz",
                    subtitle = "Test your grasp with 5 multiple-choice questions in $selectedSubject",
                    icon = Icons.Default.Quiz,
                    onClick = {
                        onLaunchStudyTopic("Create a 5-question multiple choice revision quiz for $selectedSubject with answer explanations.")
                    }
                )
            }

            item {
                StudyActionCard(
                    title = "Interactive Flashcard Deck",
                    subtitle = "Quick active-recall cards for key $selectedSubject definitions",
                    icon = Icons.Default.Book,
                    onClick = {
                        onLaunchStudyTopic("Generate a high-yield flashcard deck covering core terms and laws of $selectedSubject.")
                    }
                )
            }

            item {
                StudyActionCard(
                    title = "Exam Revision Sheet",
                    subtitle = "Concise cheat-sheet of $selectedSubject essentials and diagrams",
                    icon = Icons.Default.AutoAwesome,
                    onClick = {
                        onLaunchStudyTopic("Summarize the most critical exam topics, formulas, and common pitfalls for $selectedSubject.")
                    }
                )
            }
        }
    }
}

@Composable
fun StudyActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, VyomGlassBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = VyomAtmosphereBlue,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = VyomInkPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = VyomInkMuted,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
