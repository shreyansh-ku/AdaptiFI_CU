package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AdaptationState
import com.example.model.AppUserSettings
import com.example.model.BarrierCategory
import com.example.model.BarrierStatus
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
import com.example.ui.theme.TextMuted
import com.example.util.HapticAudioFeedbackUtil

@Composable
fun HomeScreen(
  userSettings: AppUserSettings = AppUserSettings(),
  onSettingsChanged: (AppUserSettings) -> Unit = {},
  onNavigateToBooking: () -> Unit,
  onNavigateToUnadapted: () -> Unit,
  onNavigateToCalibration: () -> Unit,
  onNavigateToSensingEngine: () -> Unit = {}
) {
  val context = LocalContext.current
  val isHindi = userSettings.selectedLanguage == "HI"

  var adaptationState by remember {
    mutableStateOf(
      AdaptationState(
        adaptiveModeEnabled = true,
        activeAdaptationsCount = 3,
        sensorLatencyMs = 18,
        textSizeAdaptation = true,
        voiceInputAdaptation = true,
        simplifiedFormAdaptation = true,
        cognitiveLoadLow = true
      )
    )
  }

  var isMicListening by remember { mutableStateOf(false) }

  val barrierCategories = remember {
    listOf(
      BarrierCategory("visual", if (isHindi) "दृष्टि" else "Vision", "visibility", BarrierStatus.DETECTED, true),
      BarrierCategory("hearing", if (isHindi) "श्रवण" else "Hearing", "hearing", BarrierStatus.NOMINAL, false),
      BarrierCategory("motor", if (isHindi) "मोटर/गति" else "Motor", "touch_app", BarrierStatus.STEADY, false),
      BarrierCategory("cognitive", if (isHindi) "संज्ञानात्मक" else "Cognitive", "psychology", BarrierStatus.DETECTED, true),
      BarrierCategory("language", if (isHindi) "भाषा" else "Language", "translate", BarrierStatus.MATCHED, false)
    )
  }

  Box(modifier = Modifier.fillMaxSize().background(Surface)) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
      // 1. Environmental Intelligence Greeting & Language Toggle Chip
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "ENVIRONMENTAL INTELLIGENCE",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = TextMuted,
              letterSpacing = 1.sp
            )
            // Demo Mode Badge
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(StateWarning.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "DEMO MODE",
                color = StateWarning,
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
              )
            }
          }

          Text(
            text = if (isHindi) "नमस्ते, यात्री" else "Good afternoon, Traveler",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface,
            letterSpacing = (-0.5).sp
          )
        }

        // Language Quick Chip (EN / HI)
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = SurfaceContainerHigh,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable {
              val newLang = if (userSettings.selectedLanguage == "HI") "EN" else "HI"
              onSettingsChanged(userSettings.copy(selectedLanguage = newLang))
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            }
            .testTag("home_language_chip")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = "Language", tint = Primary, modifier = Modifier.size(16.dp))
            Text(
              text = if (isHindi) "हिन्दी" else "English",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = Primary
            )
          }
        }
      }

      Text(
        text = if (isHindi)
          "AdaptiFi भारतीय रेल यात्रा के दौरान आपकी व्यक्तिगत आवश्यकताओं के अनुसार इंटरफ़ेस को स्वचालित रूप से अनुकूलित करता है।"
        else
          "AdaptiFi senses simulated barrier conditions in real time to adapt railway ticketing and navigation.",
        fontSize = 14.sp,
        color = OnSurfaceVariant,
        lineHeight = 20.sp
      )

      // 2. Primary Status Card: Adaptive Engine Active
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .testTag("adaptive_master_card"),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLowest,
        shadowElevation = 2.dp
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
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
                  .background(StateSuccess)
              )
              Text(
                text = if (isHindi) "अनुकूली इंजन सक्रिय" else "Adaptive Engine Active",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
            }

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = SurfaceContainer,
              modifier = Modifier
                .clickable {
                  HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
                  onNavigateToCalibration()
                }
                .testTag("calibrate_button")
            ) {
              Text(
                text = if (isHindi) "कैलिब्रेट" else "Calibrate",
                color = Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
              )
            }
          }

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
              text = if (isHindi) "सेंसर आधारित पृष्ठ अनुकूलन जारी" else "Monitoring simulated station telemetry",
              fontSize = 14.sp,
              color = OnSurfaceVariant
            )
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = StateSuccess,
                modifier = Modifier.size(16.dp)
              )
              Text(
                text = "Simulated latency ${adaptationState.sensorLatencyMs}ms • 100% on-device privacy",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          // Micro telemetry meter
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(CircleShape)
              .background(SurfaceContainerHigh)
              .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
          ) {
            Box(
              modifier = Modifier
                .weight(0.6f)
                .fillMaxSize()
                .clip(CircleShape)
                .background(Primary)
            )
            Box(
              modifier = Modifier
                .weight(0.2f)
                .fillMaxSize()
                .clip(CircleShape)
                .background(SecondaryContainer)
            )
            Box(
              modifier = Modifier
                .weight(0.2f)
                .fillMaxSize()
                .clip(CircleShape)
                .background(OutlineVariant)
            )
          }
        }
      }

      // 3. Barrier Detection Horizontal Carousel
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = if (isHindi) "पहचाने गए व्यवधान (Barriers)" else "Barrier Detection",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
          Text(
            text = "SWIPE TO REVIEW",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Primary,
            letterSpacing = 1.sp
          )
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          barrierCategories.forEach { category ->
            BarrierChipCard(category)
          }
        }
      }

      // 4. Live Adaptations Toggle List
      Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = if (isHindi) "सक्रिय अनुकूलन" else "Live Adaptations",
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(SecondaryContainer)
                .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
              Text(
                text = "${listOf(adaptationState.textSizeAdaptation, adaptationState.voiceInputAdaptation, adaptationState.simplifiedFormAdaptation).count { it }} active",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = OnSecondaryContainer
              )
            }
          }

          TextButton(
            onClick = { onNavigateToUnadapted() },
            modifier = Modifier.testTag("history_button")
          ) {
            Text(
              text = if (isHindi) "पारंपरिक ऑडिट" else "Audit Form",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Primary
            )
          }
        }

        // Adaptation 1: Text Size
        AdaptationItemCard(
          icon = Icons.Default.TextFields,
          title = if (isHindi) "टेक्स्ट आकार +40% बड़ा" else "Text size increased",
          description = if (isHindi) "प्लेटफॉर्म और स्टेशन रोशनी के अनुसार स्पष्टता" else "+40% scale applied across apps based on viewing distance.",
          badgeText = "Target: Station Glare",
          isActive = adaptationState.textSizeAdaptation,
          onToggle = {
            adaptationState = adaptationState.copy(textSizeAdaptation = it)
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
          },
          testTag = "toggle_text_size"
        )

        // Adaptation 2: Voice Input
        AdaptationItemCard(
          icon = Icons.Default.Mic,
          title = if (isHindi) "ध्वनि इनपुट सक्षम" else "Voice input enabled",
          description = if (isHindi) "स्टेशन के शोर में आवाज पहचान व डिक्टेशन" else "Acoustic speech filtering active for crowded environments.",
          badgeText = "Acoustic Filter",
          isActive = adaptationState.voiceInputAdaptation,
          onToggle = {
            adaptationState = adaptationState.copy(voiceInputAdaptation = it)
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
          },
          testTag = "toggle_voice_input"
        )

        // Adaptation 3: Simplified Form Flow
        AdaptationItemCard(
          icon = Icons.Default.Checklist,
          title = if (isHindi) "सरल 5-चरणीय रेलवे बुकिंग" else "Simplified form",
          description = if (isHindi) "संज्ञानात्मक भार कम करने हेतु एकल-उद्देश्य चरण" else "Single-intent progressive breakdown for rail ticketing.",
          badgeText = "Friction: 0",
          isActive = adaptationState.simplifiedFormAdaptation,
          onToggle = {
            adaptationState = adaptationState.copy(simplifiedFormAdaptation = it)
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
          },
          testTag = "toggle_simplified_form"
        )
      }

      // 5. Interactive Railway Booking Direct Launcher (Indian Railway Context)
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .clickable { onNavigateToBooking() }
          .testTag("launch_booking_btn"),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceTinted,
        shadowElevation = 1.dp
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Train,
                contentDescription = null,
                tint = OnPrimary,
                modifier = Modifier.size(24.dp)
              )
            }
            Column {
              Text(
                text = if (isHindi) "भारतीय रेल टिकट बुकिंग" else "Experience Railway Booking",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = if (isHindi) "नई दिल्ली (NDLS) → चंडीगढ़ (CDG) • शून्य घर्षण" else "NDLS to CDG • Accessible 5-step flow",
                fontSize = 13.sp,
                color = TextMuted
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(Primary)
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(
              text = if (isHindi) "बुक करें" else "Book",
              color = OnPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      // 6. Sensing Engine Diagnostic Card (Reachable directly from Home)
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(14.dp))
          .clickable { onNavigateToSensingEngine() }
          .testTag("home_sensing_engine_card"),
        shape = RoundedCornerShape(14.dp),
        color = PurpleAccentLight,
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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PurpleAccent),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Sensors, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(22.dp))
            }
            Column {
              Text(
                text = if (isHindi) "सेंसिंग इंजन एवं परिदृश्य डायग्नोस्टिक" else "Sensing Engine & Heuristic Scenarios",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = if (isHindi) "भीड़, धूप व कंपन परिदृश्यों का लाइव परीक्षण" else "Test Crowded Station, Glare & Tremor scenarios",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Open Sensing Engine",
            tint = PurpleAccent,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(60.dp))
    }

    // 7. Floating Action Button: 64dp Circular Voice Assistant Mic
    val micButtonColor by animateColorAsState(
      targetValue = if (isMicListening) StateSuccess else Primary,
      label = "mic_color"
    )

    FloatingActionButton(
      onClick = {
        isMicListening = !isMicListening
        HapticAudioFeedbackUtil.triggerMicChime(context)
        HapticAudioFeedbackUtil.triggerFeedback(
          context,
          if (isMicListening) HapticPatternType.RIGHT_TURN else HapticPatternType.LEFT_TURN
        )
      },
      containerColor = micButtonColor,
      contentColor = OnPrimary,
      shape = CircleShape,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(bottom = 24.dp, end = 20.dp)
        .size(64.dp)
        .testTag("fab_mic_button")
    ) {
      Icon(
        imageVector = Icons.Default.Mic,
        contentDescription = "Voice assistant dictation",
        modifier = Modifier.size(30.dp)
      )
    }
  }
}

@Composable
private fun BarrierChipCard(category: BarrierCategory) {
  val icon = when (category.iconName) {
    "visibility" -> Icons.Default.Visibility
    "hearing" -> Icons.Default.Hearing
    "touch_app" -> Icons.Default.TouchApp
    "psychology" -> Icons.Default.Psychology
    "translate" -> Icons.Default.Translate
    else -> Icons.Default.AutoAwesome
  }

  Surface(
    shape = RoundedCornerShape(14.dp),
    color = if (category.isDetected) SurfaceContainerLow else SurfaceContainerLowest,
    shadowElevation = 1.dp,
    modifier = Modifier.width(130.dp)
  ) {
    Column(
      modifier = Modifier.padding(12.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(if (category.isDetected) PrimaryFixed else SurfaceContainerHigh),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = category.name,
            tint = if (category.isDetected) Primary else TextMuted,
            modifier = Modifier.size(18.dp)
          )
        }

        Box(
          modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(if (category.isDetected) StateSuccess else TextMuted.copy(alpha = 0.5f))
        )
      }

      Column {
        Text(
          text = category.name,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = OnSurface
        )
        Text(
          text = category.status.label,
          fontSize = 11.sp,
          color = if (category.isDetected) Primary else TextMuted,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}

@Composable
private fun AdaptationItemCard(
  icon: ImageVector,
  title: String,
  description: String,
  badgeText: String,
  isActive: Boolean,
  onToggle: (Boolean) -> Unit,
  testTag: String
) {
  Surface(
    shape = RoundedCornerShape(14.dp),
    color = SurfaceContainerLowest,
    shadowElevation = 1.dp,
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isActive) PrimaryFixed else SurfaceContainerHigh),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isActive) Primary else TextMuted,
            modifier = Modifier.size(22.dp)
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Text(
              text = title,
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = OnSurface
            )
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(SurfaceContainer)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = badgeText,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = Primary
              )
            }
          }

          Text(
            text = description,
            fontSize = 12.sp,
            color = OnSurfaceVariant,
            lineHeight = 16.sp
          )
        }
      }

      Switch(
        checked = isActive,
        onCheckedChange = onToggle,
        colors = SwitchDefaults.colors(
          checkedThumbColor = OnPrimary,
          checkedTrackColor = Primary,
          uncheckedThumbColor = TextMuted,
          uncheckedTrackColor = SurfaceContainerHigh
        ),
        modifier = Modifier.testTag(testTag)
      )
    }
  }
}
