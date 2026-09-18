package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
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
fun CircleSearchOverlay(
    onActionSelected: (action: String, query: String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val points = remember { mutableStateListOf<Offset>() }
    var detectedObject by remember { mutableStateOf<String?>(null) }
    var selectionMade by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x990A0E1A)) // Translucent dim overlay
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Drawing canvas for gesture detection (⭕ Circle, ▭ Area)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            points.clear()
                            points.add(offset)
                            selectionMade = false
                        },
                        onDrag = { change, _ ->
                            points.add(change.position)
                        },
                        onDragEnd = {
                            if (points.size > 5) {
                                selectionMade = true
                                detectedObject = "Active viewport visual entity (Smart Watch & Screen Diagram)"
                            }
                        }
                    )
                }
        ) {
            if (points.size > 1) {
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }

                // Glowing outline for the drawn circle / shape
                drawPath(
                    path = path,
                    color = Color(0xFF60A5FA),
                    style = Stroke(
                        width = 8f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )

                drawPath(
                    path = path,
                    color = Color.White,
                    style = Stroke(
                        width = 3f,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }
        }

        // Top controls: Close & Tutorial tip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xEEFFFFFF))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = VyomAtmospherePurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "VYOM CIRCLE — Circle or tap anything to search",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = VyomInkPrimary
                        )
                    )
                }
            }

            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xDDFFFFFF))
                    .testTag("close_circle_overlay")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit circle search",
                    tint = VyomInkPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom Result Card (appears when user finishes drawing or selects quick action)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = VyomPureWhite,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "✦ Screen Understanding",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = VyomAtmosphereBlue
                            )
                            Text(
                                text = detectedObject ?: "Draw a circle around text or objects on your screen",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = VyomInkPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action buttons
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "SEARCH" to "Search selected content on the web",
                            "EXPLAIN" to "Explain this concept in simple terms",
                            "IDENTIFY" to "Identify the visible object",
                            "TRANSLATE" to "Translate visible text to Hindi",
                            "COMPARE" to "Compare with similar products",
                            "SUMMARIZE" to "Summarize this article"
                        ).forEach { (action, promptQuery) ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .border(1.dp, VyomGlassBorder, RoundedCornerShape(16.dp))
                                    .clickable {
                                        onActionSelected(action, promptQuery)
                                        onClose()
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("circle_action_${action.lowercase()}")
                            ) {
                                Text(
                                    text = action,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = VyomInkPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
