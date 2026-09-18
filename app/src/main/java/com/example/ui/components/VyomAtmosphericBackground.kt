package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.data.model.VyomAssistantState

@Composable
fun VyomAtmosphericBackground(
    state: VyomAssistantState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AtmosphereDrift")

    // Slow ambient translation
    val driftX by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftX"
    )

    val driftY by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 11000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "driftY"
    )

    // Pulse for thinking / searching / generating
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Atmospheric dynamic drawing
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // Base Pure White canvas
            drawRect(color = Color(0xFFFFFFFF))

            // State-based dynamic subtle aura colors
            val (glow1, glow2) = when (state) {
                VyomAssistantState.IDLE -> Pair(
                    Color(0x0C3B82F6), // Extremely faint celestial blue
                    Color(0x0A8B5CF6)  // Extremely faint purple
                )
                VyomAssistantState.THINKING -> Pair(
                    Color(0x1F3B82F6),
                    Color(0x248B5CF6)
                )
                VyomAssistantState.LISTENING -> Pair(
                    Color(0x2206B6D4), // Cyan-blue acoustic aura
                    Color(0x1A3B82F6)
                )
                VyomAssistantState.SPEAKING -> Pair(
                    Color(0x248B5CF6),
                    Color(0x203B82F6)
                )
                VyomAssistantState.SEARCHING -> Pair(
                    Color(0x223B82F6),
                    Color(0x1E06B6D4)
                )
                VyomAssistantState.GENERATING_IMAGE -> Pair(
                    Color(0x28EC4899), // Subtle magenta-celestial gradient
                    Color(0x208B5CF6)
                )
                VyomAssistantState.ERROR -> Pair(
                    Color(0x14EF4444),
                    Color(0x0C94A3B8)
                )
            }

            // Top-right subtle atmospheric light orb
            val center1 = Offset(width * 0.8f + driftX * 0.5f, height * 0.15f + driftY * 0.5f)
            val radius1 = width * 0.65f * (if (state == VyomAssistantState.THINKING) pulse else 1f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glow1, Color.Transparent),
                    center = center1,
                    radius = radius1
                ),
                center = center1,
                radius = radius1
            )

            // Bottom-left subtle celestial glow
            val center2 = Offset(width * 0.2f - driftX * 0.3f, height * 0.75f - driftY * 0.3f)
            val radius2 = width * 0.7f * (if (state == VyomAssistantState.SPEAKING) pulse else 1f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glow2, Color.Transparent),
                    center = center2,
                    radius = radius2
                ),
                center = center2,
                radius = radius2
            )

            // Extra orbital wave when searching or generating
            if (state == VyomAssistantState.SEARCHING || state == VyomAssistantState.GENERATING_IMAGE) {
                val centerMid = Offset(width * 0.5f, height * 0.45f + driftY)
                val radiusMid = width * 0.5f * pulse
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0x153B82F6), Color.Transparent),
                        center = centerMid,
                        radius = radiusMid
                    ),
                    center = centerMid,
                    radius = radiusMid
                )
            }
        }

        // Child content above the dynamic background
        content()
    }
}
