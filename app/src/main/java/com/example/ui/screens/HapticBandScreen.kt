package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FrontHand
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HapticPatternType
import com.example.ui.theme.Error
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OnTertiaryContainer
import com.example.ui.theme.OnTertiaryFixed
import com.example.ui.theme.OnTertiaryFixedVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.StateDanger
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateWarning
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.theme.TertiaryFixed
import com.example.ui.theme.TertiaryFixedDim
import com.example.ui.theme.TextMuted
import com.example.util.HapticAudioFeedbackUtil
import kotlinx.coroutines.launch

@Composable
fun HapticBandScreen() {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()

  var isConnected by remember { mutableStateOf(true) }
  var feedbackToast by remember { mutableStateOf<String?>(null) }
  var activePulsingSide by remember { mutableStateOf<String?>(null) } // "left", "right", "both"

  // Animated scale for visual haptic pulsation effect on wrist graphic
  val leftPulseAnim = remember { Animatable(1f) }
  val rightPulseAnim = remember { Animatable(1f) }

  fun triggerInteractivePattern(pattern: HapticPatternType) {
    if (!isConnected) {
      feedbackToast = "Band is disconnected. Connect to feel patterns."
      return
    }

    HapticAudioFeedbackUtil.triggerFeedback(context, pattern)

    coroutineScope.launch {
      when (pattern) {
        HapticPatternType.LEFT_TURN -> {
          activePulsingSide = "left"
          feedbackToast = "LEFT Wrist Pulse: 2 Short Cadence Pulses"
          leftPulseAnim.animateTo(1.35f, tween(120))
          leftPulseAnim.animateTo(1.0f, tween(100))
          leftPulseAnim.animateTo(1.35f, tween(120))
          leftPulseAnim.animateTo(1.0f, tween(120))
          activePulsingSide = null
        }
        HapticPatternType.RIGHT_TURN -> {
          activePulsingSide = "right"
          feedbackToast = "RIGHT Wrist Pulse: 2 Crisp Sharp Pulses"
          rightPulseAnim.animateTo(1.35f, tween(100))
          rightPulseAnim.animateTo(1.0f, tween(80))
          rightPulseAnim.animateTo(1.35f, tween(100))
          rightPulseAnim.animateTo(1.0f, tween(100))
          activePulsingSide = null
        }
        HapticPatternType.STOP_HAZARD -> {
          activePulsingSide = "both"
          feedbackToast = "URGENT STOP: Continuous Vibration Both Wrists"
          launch {
            leftPulseAnim.animateTo(1.4f, tween(250))
            leftPulseAnim.animateTo(1.0f, tween(250))
          }
          launch {
            rightPulseAnim.animateTo(1.4f, tween(250))
            rightPulseAnim.animateTo(1.0f, tween(250))
          }
          activePulsingSide = null
        }
      }
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Surface)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    // 1. Wearable Connection Card (Clear Honest Demo Label)
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 2.dp
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Status header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(if (isConnected) StateSuccess else Error)
            )
            Text(
              text = if (isConnected) "Band Simulated (BLE planned)" else "Band Disconnected",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
          }

          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(PrimaryFixed)
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = if (isConnected) "94% BATTERY" else "OFFLINE",
              color = OnPrimaryFixed,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
          }
        }

        // Animated Dual Wristband Graphic with active pulsing sides
        Surface(
          shape = RoundedCornerShape(16.dp),
          color = SurfaceContainerLow,
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Text(
              text = "Eyes-Free Navigation Band Architecture",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly,
              verticalAlignment = Alignment.CenterVertically
            ) {
              // Left Wrist Visualizer
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(72.dp)
                    .scale(leftPulseAnim.value)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (activePulsingSide == "left" || activePulsingSide == "both") Primary else PrimaryFixed),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Watch,
                    contentDescription = "Left wristband",
                    tint = if (activePulsingSide == "left" || activePulsingSide == "both") OnPrimary else Primary,
                    modifier = Modifier.size(36.dp)
                  )
                }
                Text(
                  text = "LEFT WRIST",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (activePulsingSide == "left") Primary else TextMuted
                )
                Text(
                  text = "Turn Left Cues",
                  fontSize = 11.sp,
                  color = TextMuted
                )
              }

              // Transmission Link Arrow
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Sensors,
                  contentDescription = null,
                  tint = if (isConnected) StateSuccess else TextMuted,
                  modifier = Modifier.size(24.dp)
                )
                Text(
                  text = if (isConnected) "Synced (12ms)" else "No Link",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isConnected) StateSuccess else TextMuted
                )
              }

              // Right Wrist Visualizer
              Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(72.dp)
                    .scale(rightPulseAnim.value)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (activePulsingSide == "right" || activePulsingSide == "both") Tertiary else TertiaryFixed),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.Watch,
                    contentDescription = "Right wristband",
                    tint = if (activePulsingSide == "right" || activePulsingSide == "both") OnPrimary else Tertiary,
                    modifier = Modifier.size(36.dp)
                  )
                }
                Text(
                  text = "RIGHT WRIST",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = if (activePulsingSide == "right") Tertiary else TextMuted
                )
                Text(
                  text = "Turn Right Cues",
                  fontSize = 11.sp,
                  color = TextMuted
                )
              }
            }
          }
        }

        // Callout Banner
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = InverseSurface
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(PrimaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = OnPrimaryContainer,
                modifier = Modifier.size(18.dp)
              )
            }
            Text(
              text = "Phone = environmental intelligence, Band = private tactile feedback",
              color = InverseOnSurface,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              lineHeight = 18.sp
            )
          }
        }
      }
    }

    // 2. Calibration Section Header
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Haptic Calibration",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurface
        )
        Text(
          text = "Touch to test pulse",
          fontSize = 12.sp,
          color = TextMuted
        )
      }
      Text(
        text = "Verify directional triggers for station wayfinding and platform navigation.",
        fontSize = 14.sp,
        color = OnSurfaceVariant
      )
    }

    // 3. Interactive Feedback Cards Container
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
      // Card 1: LEFT Turn
      HapticPatternCard(
        title = "LEFT Turn",
        description = "2 short vibration pulses on left wrist",
        icon = Icons.Default.TurnLeft,
        isUrgent = false,
        onTest = { triggerInteractivePattern(HapticPatternType.LEFT_TURN) },
        testTag = "test_left_turn"
      )

      // Card 2: RIGHT Turn
      HapticPatternCard(
        title = "RIGHT Turn",
        description = "2 crisp sharp pulses on right wrist",
        icon = Icons.Default.TurnRight,
        isUrgent = false,
        onTest = { triggerInteractivePattern(HapticPatternType.RIGHT_TURN) },
        testTag = "test_right_turn"
      )

      // Card 3: STOP / HAZARD
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = TertiaryFixed,
        shadowElevation = 2.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(TertiaryContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.FrontHand,
                contentDescription = null,
                tint = OnTertiaryContainer,
                modifier = Modifier.size(30.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Text(
                  text = "STOP / Hazard",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = OnTertiaryFixed
                )
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = Tertiary,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = "1 continuous long pulse (urgent)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnTertiaryFixedVariant
              )
            }
          }

          Button(
            onClick = { triggerInteractivePattern(HapticPatternType.STOP_HAZARD) },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = Tertiary,
              contentColor = OnPrimary
            ),
            modifier = Modifier
              .height(54.dp)
              .testTag("test_hazard_button")
          ) {
            Text(
              text = "Test !",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }
        }
      }
    }

    // 4. Toast Feedback Banner
    AnimatedVisibility(visible = feedbackToast != null) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = InverseSurface,
        shadowElevation = 4.dp
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(StateWarning)
            )
            Text(
              text = feedbackToast ?: "",
              color = InverseOnSurface,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
          Text(
            text = "Vibrating",
            color = PrimaryFixedDim,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }

    // 5. Disconnect / Reconnect Band Action Area
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .clickable {
            isConnected = !isConnected
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
          }
          .testTag("disconnect_band_btn"),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = if (isConnected) Icons.Default.LinkOff else Icons.Default.BluetoothConnected,
            contentDescription = null,
            tint = if (isConnected) Error else StateSuccess,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (isConnected) "Disconnect Band" else "Reconnect Band",
            color = if (isConnected) Error else StateSuccess,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.VerifiedUser,
          contentDescription = null,
          tint = TextMuted,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "BLE 5.3 Low Energy Specification • Hardware Prototype Phase",
          fontSize = 11.sp,
          color = TextMuted
        )
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

@Composable
private fun HapticPatternCard(
  title: String,
  description: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isUrgent: Boolean,
  onTest: () -> Unit,
  testTag: String
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp)),
    shape = RoundedCornerShape(16.dp),
    color = SurfaceContainerLowest,
    shadowElevation = 1.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainerHigh),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(28.dp)
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
          Text(
            text = description,
            fontSize = 13.sp,
            color = OnSurfaceVariant,
            lineHeight = 18.sp
          )
        }
      }

      Button(
        onClick = onTest,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Primary,
          contentColor = OnPrimary
        ),
        modifier = Modifier
          .height(54.dp)
          .testTag(testTag)
      ) {
        Text(
          text = "Test",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
      }
    }
  }
}
