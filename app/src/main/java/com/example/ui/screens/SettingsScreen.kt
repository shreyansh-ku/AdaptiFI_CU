package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppUserSettings
import com.example.model.HapticPatternType
import com.example.ui.theme.Error
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleAccentLight
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.StateWarning
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceTinted
import com.example.ui.theme.TextMuted
import com.example.util.HapticAudioFeedbackUtil

@Composable
fun SettingsScreen(
  userSettings: AppUserSettings,
  onSettingsChanged: (AppUserSettings) -> Unit,
  onResetDemo: () -> Unit,
  onOpenSensingEngine: () -> Unit
) {
  val context = LocalContext.current
  val isHindi = userSettings.selectedLanguage == "HI"

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Surface)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp)
  ) {
    // 1. Header Card with Demo Badge
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("telemetry_diagnostic_header"),
      shape = RoundedCornerShape(16.dp),
      color = SurfaceContainerLow,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
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
                imageVector = Icons.Default.Tune,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(24.dp)
              )
            }
            Column {
              Text(
                text = if (isHindi) "अभिगम्यता सेटिंग्स" else "Accessibility Preferences",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = if (isHindi) "अनुकूली डिस्प्ले एवं हैप्टिक्स" else "Adaptive Display & Haptics",
                fontSize = 13.sp,
                color = TextMuted
              )
            }
          }

          // Demo Mode pill badge
          Box(
            modifier = Modifier
              .clip(CircleShape)
              .background(StateWarning.copy(alpha = 0.15f))
              .padding(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Text(
              text = "DEMO MODE",
              color = StateWarning,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        Text(
          text = if (isHindi)
            "स्मार्ट इंडिया हैकाथॉन: बाधा-मुक्त तकनीक के लिए व्यक्तिगत सहायता।"
          else
            "Smart India Hackathon: Real-time UI and sensory adaptation for barrier-free interaction.",
          fontSize = 13.sp,
          color = OnSurfaceVariant,
          lineHeight = 18.sp
        )
      }
    }

    // 2. Language Selector
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
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Icon(Icons.Default.Language, contentDescription = null, tint = Primary, modifier = Modifier.size(22.dp))
          Text(
            text = if (isHindi) "भाषा चयन (Language)" else "Interface Language",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
        }

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // English Chip
          val isEnSelected = userSettings.selectedLanguage == "EN"
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isEnSelected) Primary else SurfaceContainerHigh,
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .clickable {
                onSettingsChanged(userSettings.copy(selectedLanguage = "EN"))
                HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
              }
              .testTag("lang_en_chip")
          ) {
            Box(
              modifier = Modifier.padding(vertical = 12.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "English",
                color = if (isEnSelected) OnPrimary else OnSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }

          // Hindi Chip
          val isHiSelected = userSettings.selectedLanguage == "HI"
          Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isHiSelected) Primary else SurfaceContainerHigh,
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(10.dp))
              .clickable {
                onSettingsChanged(userSettings.copy(selectedLanguage = "HI"))
                HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
              }
              .testTag("lang_hi_chip")
          ) {
            Box(
              modifier = Modifier.padding(vertical = 12.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "हिन्दी (Hindi)",
                color = if (isHiSelected) OnPrimary else OnSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
              )
            }
          }
        }
      }
    }

    // 3. Text Size Slider (100% to 160%)
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
            Icon(Icons.Default.FormatSize, contentDescription = null, tint = Primary, modifier = Modifier.size(22.dp))
            Text(
              text = if (isHindi) "टेक्स्ट आकार (Text Scale)" else "Text Scale",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
          }

          Text(
            text = "${(userSettings.fontScale * 100).toInt()}%",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Primary
          )
        }

        Slider(
          value = userSettings.fontScale,
          onValueChange = { scale ->
            onSettingsChanged(userSettings.copy(fontScale = scale))
          },
          valueRange = 1.0f..1.6f,
          steps = 5,
          colors = SliderDefaults.colors(
            thumbColor = Primary,
            activeTrackColor = Primary,
            inactiveTrackColor = SurfaceContainerHigh
          ),
          modifier = Modifier.testTag("slider_text_scale")
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("100% (Standard)", fontSize = 12.sp, color = TextMuted)
          Text("130% (Recommended)", fontSize = 12.sp, color = Primary, fontWeight = FontWeight.Bold)
          Text("160% (Max)", fontSize = 12.sp, color = TextMuted)
        }
      }
    }

    // 4. High-Contrast Toggle & Voice Prompts Toggle
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // High-Contrast
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Icon(Icons.Default.Contrast, contentDescription = null, tint = PurpleAccent, modifier = Modifier.size(22.dp))
            Column {
              Text(
                text = if (isHindi) "उच्च कंट्रास्ट मोड (High Contrast)" else "High Contrast Mode",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = if (isHindi) "कम रोशनी व धूप के लिए 7:1 कंट्रास्ट" else "WCAG AAA 7:1 ratio for glare and low vision",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          Switch(
            checked = userSettings.highContrast,
            onCheckedChange = { checked ->
              onSettingsChanged(userSettings.copy(highContrast = checked))
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            colors = SwitchDefaults.colors(
              checkedThumbColor = OnPrimary,
              checkedTrackColor = Primary
            ),
            modifier = Modifier.testTag("toggle_high_contrast")
          )
        }

        // Voice Prompts
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Icon(Icons.Default.VolumeUp, contentDescription = null, tint = Secondary, modifier = Modifier.size(22.dp))
            Column {
              Text(
                text = if (isHindi) "वॉयस प्रॉम्प्ट्स (Voice Prompts)" else "Spoken Voice Prompts",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
              )
              Text(
                text = if (isHindi) "प्रत्येक चरण पर श्रव्य निर्देश" else "Audio readouts for turns and station steps",
                fontSize = 12.sp,
                color = TextMuted
              )
            }
          }

          Switch(
            checked = userSettings.voicePromptsEnabled,
            onCheckedChange = { checked ->
              onSettingsChanged(userSettings.copy(voicePromptsEnabled = checked))
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
            },
            colors = SwitchDefaults.colors(
              checkedThumbColor = OnPrimary,
              checkedTrackColor = Secondary
            ),
            modifier = Modifier.testTag("toggle_voice_prompts")
          )
        }
      }
    }

    // 5. Haptic Intensity Slider
    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(14.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
            Icon(Icons.Default.Vibration, contentDescription = null, tint = Tertiary, modifier = Modifier.size(22.dp))
            Text(
              text = if (isHindi) "हैप्टिक तीव्रता (Vibration)" else "Haptic Feedback Strength",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
          }

          Text(
            text = "${(userSettings.hapticIntensity * 100).toInt()}%",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Tertiary
          )
        }

        Slider(
          value = userSettings.hapticIntensity,
          onValueChange = { intensity ->
            onSettingsChanged(userSettings.copy(hapticIntensity = intensity))
          },
          valueRange = 0.2f..1.0f,
          colors = SliderDefaults.colors(
            thumbColor = Tertiary,
            activeTrackColor = Tertiary,
            inactiveTrackColor = SurfaceContainerHigh
          ),
          modifier = Modifier.testTag("slider_haptic_strength")
        )
      }
    }

    // 6. Navigation to Sensing Engine Heuristics Screen
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .clickable { onOpenSensingEngine() }
        .testTag("open_sensing_engine_btn"),
      shape = RoundedCornerShape(14.dp),
      color = PurpleAccentLight,
      shadowElevation = 1.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
              text = if (isHindi) "सेंसिंग इंजन डायग्नोस्टिक" else "Sensing Engine & Heuristics",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = OnSurface
            )
            Text(
              text = if (isHindi) "लाइव सेंसर एवं परिदृश्य ट्रिगर्स" else "Inspect simulated telemetry & scenarios",
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

    // 7. Reset Demo Action Button
    Button(
      onClick = {
        onResetDemo()
        HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.STOP_HAZARD)
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .testTag("reset_demo_button"),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Error.copy(alpha = 0.1f),
        contentColor = Error
      )
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
        Text(
          text = if (isHindi) "डेमो रीसेट करें (Reset Demo)" else "Reset Demo to Defaults",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
