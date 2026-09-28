package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdaptationState
import com.example.model.HapticPatternType
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleAccentLight
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.StateDanger
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateWarning
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceCanvas
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceTinted
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.ui.theme.TertiaryFixed
import com.example.ui.theme.TertiaryFixedDim
import com.example.ui.theme.TextMuted
import com.example.util.HapticAudioFeedbackUtil
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun SensingEngineScreen(
  adaptationState: AdaptationState = AdaptationState(),
  onAdaptationStateChanged: (AdaptationState) -> Unit = {},
  onNavigateToBooking: () -> Unit = {},
  onBack: (() -> Unit)? = null
) {
  val context = LocalContext.current

  // Live Simulated Values with smooth random walk
  var noiseDb by remember { mutableIntStateOf(adaptationState.ambientNoiseDb) }
  var gazeDwell by remember { mutableFloatStateOf(adaptationState.gazeDwellSec) }
  var tremorHz by remember { mutableFloatStateOf(adaptationState.handTremorHz) }
  var heartRate by remember { mutableIntStateOf(adaptationState.heartRateBpm) }

  var visualBarrierPct by remember { mutableIntStateOf(adaptationState.visualBarrierProb) }
  var acousticBarrierPct by remember { mutableIntStateOf(adaptationState.acousticBarrierProb) }
  var motorBarrierPct by remember { mutableIntStateOf(adaptationState.motorTremorBarrierProb) }

  var activeScenarioMessage by remember { mutableStateOf<String?>(null) }
  var showPipelineRules by remember { mutableStateOf(false) }

  // Smooth periodic random walk for simulated sensors
  LaunchedEffect(Unit) {
    while (true) {
      delay(2200)
      noiseDb = (noiseDb + Random.nextInt(-3, 4)).coerceIn(40, 85)
      gazeDwell = ((gazeDwell + Random.nextFloat() * 0.4f - 0.2f)).coerceIn(0.8f, 3.4f)
      tremorHz = ((tremorHz + Random.nextFloat() * 0.3f - 0.15f)).coerceIn(1.5f, 6.5f)
      heartRate = (heartRate + Random.nextInt(-2, 3)).coerceIn(60, 95)

      // Re-evaluate probabilities
      acousticBarrierPct = ((noiseDb - 35) * 1.6f).toInt().coerceIn(15, 95)
      motorBarrierPct = ((tremorHz - 1.5f) * 16f).toInt().coerceIn(10, 90)
      visualBarrierPct = ((gazeDwell - 0.8f) * 28f).toInt().coerceIn(20, 92)
    }
  }

  // Audio wave visualizer animation
  val infiniteTransition = rememberInfiniteTransition(label = "audio_bars")
  val wave1 by infiniteTransition.animateFloat(
    initialValue = 4f,
    targetValue = 18f,
    animationSpec = infiniteRepeatable(tween(400, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "wave1"
  )
  val wave2 by infiniteTransition.animateFloat(
    initialValue = 14f,
    targetValue = 6f,
    animationSpec = infiniteRepeatable(tween(350, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "wave2"
  )
  val wave3 by infiniteTransition.animateFloat(
    initialValue = 8f,
    targetValue = 20f,
    animationSpec = infiniteRepeatable(tween(500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "wave3"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Surface)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Back button (if presented modally/from settings) and Header
    if (onBack != null) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("sensing_back_button")
        ) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Primary)
        }
        Text(
          text = "Sensing Engine Diagnostics",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurface
        )
      }
    }

    // 2. Status Overview Header Banner (with DEMO MODE: SIMULATED SENSORS badge)
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("telemetry_diagnostic_header"),
      shape = RoundedCornerShape(16.dp),
      color = SurfaceContainerLow,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PrimaryFixed),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Insights,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(24.dp)
              )
            }
            Column {
              Text(
                text = "Adaptive Sensing Engine",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = "On-Device Heuristic Classifier",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          // DEMO MODE: SIMULATED SENSORS BADGE
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(StateWarning.copy(alpha = 0.15f))
              .padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Text(
              text = "DEMO: SIMULATED",
              color = StateWarning,
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 0.5.sp
            )
          }
        }

        Text(
          text = "All telemetry streams are simulated live. Sensor values drive the real-time transformation rules without cloud dependencies.",
          fontSize = 12.sp,
          color = OnSurfaceVariant,
          lineHeight = 16.sp
        )
      }
    }

    // 3. Trigger Scenario Action Row (Hackathon Demo Requirement)
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Bolt, contentDescription = null, tint = Tertiary, modifier = Modifier.size(20.dp))
            Text(
              text = "Trigger Demo Scenarios",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
          }

          IconButton(
            onClick = {
              noiseDb = 52
              gazeDwell = 1.4f
              tremorHz = 3.2f
              acousticBarrierPct = 34
              motorBarrierPct = 22
              visualBarrierPct = 40
              activeScenarioMessage = "Simulated sensors returned to baseline nominal values."
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier.testTag("refresh_telemetry_btn")
          ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh telemetry", tint = Primary)
          }
        }

        Text(
          text = "Simulate real-world Indian railway station conditions to test immediate automated UI adaptations:",
          fontSize = 13.sp,
          color = TextMuted
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Scenario 1: Crowded Station
          Button(
            onClick = {
              noiseDb = 78
              acousticBarrierPct = 86
              onAdaptationStateChanged(
                adaptationState.copy(
                  voiceInputAdaptation = true,
                  ambientNoiseDb = 78,
                  acousticBarrierProb = 86
                )
              )
              activeScenarioMessage = "Rule Fired: Ambient Noise > 65 dB → Voice input & Subtitles Auto-Enabled"
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("scenario_crowded_station"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
          ) {
            Text("Crowded Station", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          // Scenario 2: Bright Sunlight
          Button(
            onClick = {
              gazeDwell = 2.9f
              visualBarrierPct = 88
              onAdaptationStateChanged(
                adaptationState.copy(
                  textSizeAdaptation = true,
                  gazeDwellSec = 2.9f,
                  visualBarrierProb = 88
                )
              )
              activeScenarioMessage = "Rule Fired: Glare / Gaze Dwell > 2.5s → High-Contrast & Text Scale Auto-Applied"
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("scenario_bright_sunlight"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
          ) {
            Text("Bright Sunlight", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }

          // Scenario 3: Hand Tremor
          Button(
            onClick = {
              tremorHz = 5.8f
              motorBarrierPct = 84
              onAdaptationStateChanged(
                adaptationState.copy(
                  simplifiedFormAdaptation = true,
                  handTremorHz = 5.8f,
                  motorTremorBarrierProb = 84
                )
              )
              activeScenarioMessage = "Rule Fired: Hand Tremor > 4.5 Hz → Touch Targets expanded to 56dp"
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.STOP_HAZARD)
            },
            modifier = Modifier
              .weight(1f)
              .height(48.dp)
              .testTag("scenario_hand_tremor"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Tertiary)
          ) {
            Text("Hand Tremor", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }

        // Active Scenario Banner
        AnimatedVisibility(visible = activeScenarioMessage != null) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Primary, modifier = Modifier.size(16.dp))
              Text(
                text = activeScenarioMessage ?: "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
            }
          }
        }
      }
    }

    // 4. Live Sensor Telemetry Cards (Simulated Stream)
    Text(
      text = "Simulated Sensor Streams (Live Walk)",
      fontSize = 15.sp,
      fontWeight = FontWeight.Bold,
      color = OnSurface
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      // Noise
      Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("NOISE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
          Text("$noiseDb dB", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = if (noiseDb > 70) StateDanger else Primary)
          Text(if (noiseDb > 70) "High Ambience" else "Moderate", fontSize = 11.sp, color = TextMuted)
        }
      }

      // Tremor
      Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("TREMOR", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
          Text(String.format("%.1f Hz", tremorHz), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = if (tremorHz > 4.5f) StateDanger else Tertiary)
          Text(if (tremorHz > 4.5f) "Instability" else "Nominal", fontSize = 11.sp, color = TextMuted)
        }
      }

      // Gaze
      Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 1.dp
      ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text("GAZE DWELL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
          Text(String.format("%.1f s", gazeDwell), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = if (gazeDwell > 2.5f) StateWarning else Secondary)
          Text(if (gazeDwell > 2.5f) "Fatigue/Search" else "Focused", fontSize = 11.sp, color = TextMuted)
        }
      }
    }

    // 5. Barrier Probability Matrix
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Barrier Probabilities", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = OnSurface)
          Text("WCAG 2.2 AAA Target", fontSize = 11.sp, color = Secondary, fontWeight = FontWeight.Bold)
        }

        // Barrier 1: Visual / Glare
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Visual Inaccessibility (Sunlight / Contrast)", fontSize = 13.sp, color = OnSurface)
            Text("$visualBarrierPct%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Primary)
          }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHigh)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(visualBarrierPct / 100f)
                .height(6.dp)
                .clip(CircleShape)
                .background(Primary)
            )
          }
        }

        // Barrier 2: Acoustic Noise
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Acoustic Inaccessibility (Railway Announcements / Echo)", fontSize = 13.sp, color = OnSurface)
            Text("$acousticBarrierPct%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = PurpleAccent)
          }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHigh)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(acousticBarrierPct / 100f)
                .height(6.dp)
                .clip(CircleShape)
                .background(PurpleAccent)
            )
          }
        }

        // Barrier 3: Motor / Tremor
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Motor Precision Barrier (Walking / Tremor)", fontSize = 13.sp, color = OnSurface)
            Text("$motorBarrierPct%", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Tertiary)
          }
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHigh)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(motorBarrierPct / 100f)
                .height(6.dp)
                .clip(CircleShape)
                .background(Tertiary)
            )
          }
        }
      }
    }

    // 6. Action Button: Inspect Adaptation Pipeline Rules
    Button(
      onClick = { showPipelineRules = !showPipelineRules },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .testTag("inspect_pipeline_btn"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = SurfaceContainerLow,
        contentColor = Primary
      )
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = if (showPipelineRules) "Hide Pipeline Rules" else "Inspect Adaptation Pipeline",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowForward,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
      }
    }

    if (showPipelineRules) {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SurfaceCanvas.copy(alpha = 0.95f),
        shadowElevation = 2.dp
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Text(
            text = "Active Heuristic Pipeline Rules",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = OnSurface
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
              text = "1. Gaze Dwell > 2.5s → Trigger Progressive Form Decomposition (1 intent/step)",
              fontSize = 13.sp,
              color = OnSurfaceVariant
            )
            Text(
              text = "2. Ambient Noise > 65 dB → Switch to Acoustic Isolation & Visual Badges",
              fontSize = 13.sp,
              color = OnSurfaceVariant
            )
            Text(
              text = "3. Motor Hesitation / Tremor > 4.5 Hz → Expand Touch Boundaries to 56dp+",
              fontSize = 13.sp,
              color = OnSurfaceVariant
            )
            Text(
              text = "4. Transit Heading Alert → Dispatch Dual-Pulse Haptic to Left/Right Wrist",
              fontSize = 13.sp,
              color = OnSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Button(
            onClick = onNavigateToBooking,
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
          ) {
            Text("Launch Railway Booking Flow", fontSize = 14.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}
