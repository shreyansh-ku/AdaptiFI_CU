package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ARWaypoint
import com.example.model.HapticPatternType
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.StateDanger
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TextMuted
import com.example.util.HapticAudioFeedbackUtil

@Composable
fun ARNavigationScreen(
  onNavigateToBand: () -> Unit
) {
  val context = LocalContext.current

  var highContrastEnabled by remember { mutableStateOf(false) }
  var flashlightEnabled by remember { mutableStateOf(false) }
  var zoomFactor by remember { mutableFloatStateOf(1f) }
  var hapticToastText by remember { mutableStateOf<String?>(null) }

  // Indian Railway Station Waypoints
  val waypoints = remember {
    listOf(
      ARWaypoint("wp1", "Turn left to Platform 3", "Dual pulse sent to left wristband", 40, "PLATFORM 3", HapticPatternType.LEFT_TURN),
      ARWaypoint("wp2", "Continue through station concourse", "Gentle forward walking cadence", 25, "CONCOURSE EAST", HapticPatternType.RIGHT_TURN),
      ARWaypoint("wp3", "Caution: Approaching stairs / accessible ramp", "Urgent stop hazard alert", 8, "COACH C2 OVERPASS", HapticPatternType.STOP_HAZARD),
      ARWaypoint("wp4", "Arrived at Coach C2 Boarding Point", "Door alignment confirmed", 0, "COACH C2 / SEAT 12", HapticPatternType.RIGHT_TURN)
    )
  }

  var currentWpIndex by remember { mutableIntStateOf(0) }
  val currentWaypoint = waypoints[currentWpIndex]

  // Animated perspective chevrons
  val infiniteTransition = rememberInfiniteTransition(label = "chevrons")
  val chevronOffset by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "chevron_flow"
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(if (highContrastEnabled) Color(0xFF000000) else Color(0xFF0F172A))
  ) {
    // 1. First-Person Perspective Railway Station Canvas
    Canvas(
      modifier = Modifier
        .fillMaxSize()
        .clickable {
          // Advance to next waypoint on tap
          currentWpIndex = (currentWpIndex + 1) % waypoints.size
          val next = waypoints[currentWpIndex]
          HapticAudioFeedbackUtil.triggerFeedback(context, next.pattern)
          hapticToastText = "Haptic feedback triggered: ${next.pattern.name.replace('_', ' ')}"
        }
    ) {
      val w = size.width
      val h = size.height
      val vanishPoint = Offset(w * 0.5f, h * 0.42f)

      // Perspective station platform concourse
      val leftFloor = Offset(0f, h)
      val rightFloor = Offset(w, h)
      val pathFloor = Path().apply {
        moveTo(leftFloor.x, leftFloor.y)
        lineTo(vanishPoint.x, vanishPoint.y)
        lineTo(rightFloor.x, rightFloor.y)
        close()
      }

      drawPath(
        path = pathFloor,
        brush = Brush.verticalGradient(
          colors = if (highContrastEnabled) {
            listOf(Color(0xFF0A0F1D), Color(0xFF1E293B))
          } else {
            listOf(Color(0xFF1E293B), Color(0xFF334155))
          },
          startY = vanishPoint.y,
          endY = h
        )
      )

      // Tactile guide path on railway platform floor
      val guidePath = Path().apply {
        moveTo(w * 0.46f, h)
        lineTo(vanishPoint.x - 2f, vanishPoint.y)
        lineTo(vanishPoint.x + 2f, vanishPoint.y)
        lineTo(w * 0.54f, h)
        close()
      }
      drawPath(
        path = guidePath,
        color = if (highContrastEnabled) Color(0xFFFBBF24).copy(alpha = 0.85f) else Color(0xFF60A5FA).copy(alpha = 0.5f)
      )

      // Animated arrows / chevrons towards Coach C2
      for (i in 0..4) {
        val prog = (chevronOffset + i * 0.25f) % 1f
        val y = vanishPoint.y + (h - vanishPoint.y) * prog
        val spread = (w * 0.35f) * prog
        val arrowPath = Path().apply {
          moveTo(vanishPoint.x - spread, y + 20f * prog)
          lineTo(vanishPoint.x, y)
          lineTo(vanishPoint.x + spread, y + 20f * prog)
        }
        drawPath(
          path = arrowPath,
          color = if (highContrastEnabled) Color(0xFF34D399).copy(alpha = 1f - prog) else Color(0xFF93C5FD).copy(alpha = 1f - prog),
          style = Stroke(width = (4f + 6f * prog))
        )
      }

      // Neon AR Station Bounding Box (Platform Signage)
      val reticleTop = h * 0.28f
      val reticleWidth = w * 0.62f
      val reticleHeight = 70f
      val reticleLeft = (w - reticleWidth) / 2f

      drawRoundRect(
        color = if (highContrastEnabled) Color(0xFF34D399) else Color(0xFF38BDF8),
        topLeft = Offset(reticleLeft, reticleTop),
        size = Size(reticleWidth, reticleHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f),
        style = Stroke(width = 3.5f)
      )
    }

    // 2. Flashlight & Zoom Overlays
    if (flashlightEnabled) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color.White.copy(alpha = 0.15f))
      )
    }

    // 3. Top HUD Status Bar (Railway Station Context)
    Column(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(horizontal = 16.dp, vertical = 14.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(24.dp),
        color = InverseSurface.copy(alpha = 0.85f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(StateSuccess)
            )
            Text(
              text = "NDLS PLATFORM 3 • COACH C2",
              color = InverseOnSurface,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }

          // Demo Badge
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Tertiary.copy(alpha = 0.2f))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text(
              text = "SIMULATED AR",
              color = Tertiary,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // AR Vision Controls (Contrast, Flashlight, Zoom)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(InverseSurface.copy(alpha = 0.8f))
            .padding(horizontal = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          IconButton(
            onClick = {
              highContrastEnabled = !highContrastEnabled
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier.testTag("ar_contrast_toggle")
          ) {
            Icon(
              imageVector = Icons.Default.Contrast,
              contentDescription = "Contrast toggle",
              tint = if (highContrastEnabled) StateSuccess else Color.White
            )
          }

          IconButton(
            onClick = {
              flashlightEnabled = !flashlightEnabled
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier.testTag("ar_flashlight_toggle")
          ) {
            Icon(
              imageVector = Icons.Default.FlashlightOn,
              contentDescription = "Flashlight toggle",
              tint = if (flashlightEnabled) StateSuccess else Color.White
            )
          }

          IconButton(
            onClick = {
              zoomFactor = if (zoomFactor == 1f) 2f else 1f
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier.testTag("ar_zoom_toggle")
          ) {
            Text(
              text = if (zoomFactor == 1f) "1×" else "2×",
              color = if (zoomFactor > 1f) StateSuccess else Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }

    // 4. Floating AR Signboard (Locked to Railway Destination)
    Box(
      modifier = Modifier
        .align(Alignment.TopCenter)
        .padding(top = 180.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = InverseSurface.copy(alpha = 0.9f),
        shadowElevation = 4.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Train, contentDescription = null, tint = Primary, modifier = Modifier.size(20.dp))
          Text(
            text = currentWaypoint.platformLabel,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            letterSpacing = 1.sp
          )
          Text(
            text = "${currentWaypoint.distanceMeters}m",
            color = PrimaryFixedDim,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // 5. Toast Feedback
    AnimatedVisibility(
      visible = hapticToastText != null,
      modifier = Modifier
        .align(Alignment.Center)
        .padding(horizontal = 24.dp)
    ) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = InverseSurface,
        shadowElevation = 6.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Vibration, contentDescription = null, tint = StateSuccess)
          Text(
            text = hapticToastText ?: "",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
      }
    }

    // 6. Bottom Navigation Instruction Card
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("ar_bottom_card"),
      shape = RoundedCornerShape(20.dp),
      color = InverseSurface.copy(alpha = 0.95f),
      shadowElevation = 8.dp
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(Primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = when (currentWaypoint.pattern) {
                  HapticPatternType.LEFT_TURN -> Icons.Default.TurnLeft
                  HapticPatternType.STOP_HAZARD -> Icons.Default.Sensors
                  else -> Icons.AutoMirrored.Filled.DirectionsWalk
                },
                contentDescription = null,
                tint = OnPrimary,
                modifier = Modifier.size(26.dp)
              )
            }

            Column {
              Text(
                text = currentWaypoint.instruction,
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = currentWaypoint.subtext,
                color = InverseOnSurface.copy(alpha = 0.7f),
                fontSize = 13.sp
              )
            }
          }

          // Replay audio button
          IconButton(
            onClick = {
              HapticAudioFeedbackUtil.triggerFeedback(context, currentWaypoint.pattern)
              hapticToastText = "Audio announcement: ${currentWaypoint.instruction}"
            },
            modifier = Modifier.testTag("ar_replay_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.VolumeUp,
              contentDescription = "Replay audio guidance",
              tint = PrimaryFixedDim,
              modifier = Modifier.size(24.dp)
            )
          }
        }

        // Tap to advance hint
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .clickable {
              currentWpIndex = (currentWpIndex + 1) % waypoints.size
              val next = waypoints[currentWpIndex]
              HapticAudioFeedbackUtil.triggerFeedback(context, next.pattern)
            }
            .padding(horizontal = 12.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Waypoint ${currentWpIndex + 1} of ${waypoints.size} • Tap screen to simulate walking step",
            color = InverseOnSurface,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = "NEXT",
            color = PrimaryFixedDim,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
