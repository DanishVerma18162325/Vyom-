package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.VyomAssistantState
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VyomOrb(
    state: VyomAssistantState,
    audioRms: Float = 0f,
    size: Dp = 120.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "VyomOrbMotion")

    // Slow orbital rotation (Thinking / Idle)
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = if (state == VyomAssistantState.THINKING) 3200 else 18000,
                easing = LinearEasing
            )
        ),
        label = "rotationAngle"
    )

    // Breathing pulse for listening
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    // Wave ring shimmer
    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing)
        ),
        label = "shimmerPhase"
    )

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val center = Offset(w / 2f, h / 2f)
        val baseRadius = (w.coerceAtMost(h) / 2f) * 0.72f

        // State-driven dynamics
        val dynamicScale = when (state) {
            VyomAssistantState.LISTENING -> pulseScale
            VyomAssistantState.SPEAKING -> 1f + (audioRms * 0.28f)
            VyomAssistantState.THINKING -> 0.98f
            VyomAssistantState.SEARCHING -> pulseScale * 1.04f
            VyomAssistantState.GENERATING_IMAGE -> pulseScale
            else -> 1f
        }

        val effectiveRadius = baseRadius * dynamicScale

        // 1. Outermost Ambient Atmospheric Aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    when (state) {
                        VyomAssistantState.LISTENING -> Color(0x3506B6D4)
                        VyomAssistantState.SPEAKING -> Color(0x388B5CF6)
                        VyomAssistantState.THINKING -> Color(0x333B82F6)
                        VyomAssistantState.GENERATING_IMAGE -> Color(0x35EC4899)
                        else -> Color(0x203B82F6)
                    },
                    Color.Transparent
                ),
                center = center,
                radius = effectiveRadius * 1.45f
            ),
            center = center,
            radius = effectiveRadius * 1.45f
        )

        // 2. Dynamic Concentric Wave Rings (Listening or Speaking)
        if (state == VyomAssistantState.LISTENING || state == VyomAssistantState.SPEAKING) {
            val ringRadius = effectiveRadius * (1.12f + shimmerPhase * 0.35f)
            val ringAlpha = (1f - shimmerPhase) * 0.45f
            drawCircle(
                color = Color(0xFF3B82F6).copy(alpha = ringAlpha),
                center = center,
                radius = ringRadius,
                style = Stroke(width = 2.5f)
            )
        }

        // 3. Rotating Orbital Planetary Ring
        rotate(degrees = rotationAngle, pivot = center) {
            // Elliptical orbital path
            drawOval(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF3B82F6),
                        Color(0xFF8B5CF6),
                        Color(0xFF06B6D4),
                        Color(0x203B82F6),
                        Color(0xFF3B82F6)
                    ),
                    center = center
                ),
                topLeft = Offset(center.x - effectiveRadius * 1.15f, center.y - effectiveRadius * 0.55f),
                size = androidx.compose.ui.geometry.Size(effectiveRadius * 2.3f, effectiveRadius * 1.1f),
                style = Stroke(width = 3f)
            )

            // Orbital satellites / energy nodes
            val rad = Math.toRadians(rotationAngle.toDouble())
            val satX = center.x + (effectiveRadius * 1.15f) * cos(rad).toFloat()
            val satY = center.y + (effectiveRadius * 0.55f) * sin(rad).toFloat()
            drawCircle(
                color = Color(0xFF06B6D4),
                center = Offset(satX, satY),
                radius = 4.5f
            )
        }

        // 4. Core Celestial Sphere
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFF1F5F9),
                    Color(0xFFE0E7FF),
                    Color(0xFFC7D2FE)
                ),
                center = Offset(center.x - effectiveRadius * 0.25f, center.y - effectiveRadius * 0.25f),
                radius = effectiveRadius
            ),
            center = center,
            radius = effectiveRadius
        )

        // Core Sphere Inner Boundary Glow
        drawCircle(
            color = Color(0xFF6366F1).copy(alpha = 0.22f),
            center = center,
            radius = effectiveRadius,
            style = Stroke(width = 2f)
        )

        // 5. Signature Abstract "V" (Vyom Akash Emblem)
        // Clean geometric lines representing the letter V ascending into infinity
        rotate(degrees = if (state == VyomAssistantState.THINKING) rotationAngle * 0.25f else 0f, pivot = center) {
            val vPath = Path().apply {
                val vTopY = center.y - effectiveRadius * 0.42f
                val vBottomY = center.y + effectiveRadius * 0.42f
                val vLeftX = center.x - effectiveRadius * 0.38f
                val vRightX = center.x + effectiveRadius * 0.38f

                moveTo(vLeftX, vTopY)
                lineTo(center.x, vBottomY)
                lineTo(vRightX, vTopY)
            }

            // Outer V glow stroke
            drawPath(
                path = vPath,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6)),
                    startY = center.y - effectiveRadius * 0.5f,
                    endY = center.y + effectiveRadius * 0.5f
                ),
                style = Stroke(
                    width = 6f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Inner bright V core
            drawPath(
                path = vPath,
                color = Color.White,
                style = Stroke(
                    width = 2.5f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Apex star / beacon at top
            drawCircle(
                color = Color(0xFF38BDF8),
                center = Offset(center.x, center.y - effectiveRadius * 0.15f),
                radius = 3.5f
            )
        }
    }
}
