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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Error
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnErrorContainer
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryFixed
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.StateDanger
import com.example.ui.theme.StateWarning
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TextMuted

@Composable
fun UnadaptedBookingScreen(
  onBack: () -> Unit,
  onSwitchToAdapted: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Surface)
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. Top Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("unadapted_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Primary
          )
        }
        Column {
          Text(
            text = "Legacy Rail Portal Audit",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
          Text(
            text = "Baseline Inaccessibility Benchmark",
            fontSize = 12.sp,
            color = TextMuted
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(20.dp),
        color = SecondaryFixed,
        modifier = Modifier
          .clickable { onSwitchToAdapted() }
          .testTag("switch_to_adapted_btn")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.AutoFixHigh,
            contentDescription = null,
            tint = Secondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "Adapted Mode",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Secondary
          )
        }
      }
    }

    // 2. High Cognitive Load Banner
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = ErrorContainer,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = null,
          tint = Error,
          modifier = Modifier.size(24.dp)
        )
        Column {
          Text(
            text = "8 Critical Accessibility Bottlenecks Detected",
            color = OnErrorContainer,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "Small touch areas (<32dp), 2:1 low contrast text, CAPTCHA fatigue, and 5-min session timeout timer.",
            color = OnErrorContainer,
            fontSize = 12.sp
          )
        }
      }
    }

    // 3. Cluttered Benchmark Form Card
    Surface(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      shape = RoundedCornerShape(16.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 1.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Ticket Booking",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = OnSurface
        )

        // Mock Cluttered Dense Rows
        BenchmarkFormField(
          label = "Passenger 1 Full Name *",
          value = "Aarav Sharma",
          issueBadge = "Tiny font (11sp) & missing voice dictation"
        )

        BenchmarkFormField(
          label = "Aadhaar / ID Card Number *",
          value = "XXXX-XXXX-4821",
          issueBadge = "No auto-masking or camera scan support"
        )

        BenchmarkFormField(
          label = "Origin & Destination Station Codes",
          value = "NDLS (New Delhi) → CDG (Chandigarh)",
          issueBadge = "High jargon, no audio route preview"
        )

        BenchmarkFormField(
          label = "Quota Selection & Verification",
          value = "Divyangjan / Senior Citizen Priority",
          issueBadge = "Multi-nested dropdown with 28dp touch target"
        )

        // Simulated Captcha Bottleneck
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = SurfaceContainerHigh,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Security CAPTCHA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              Text("g X 9 4 P Q", fontSize = 16.sp, letterSpacing = 4.sp, fontWeight = FontWeight.Black, color = TextMuted)
            }
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(Error.copy(alpha = 0.15f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("Exclusion Barrier", color = Error, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    // 4. Switch to Adapted Mode CTA
    Button(
      onClick = onSwitchToAdapted,
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp),
      shape = RoundedCornerShape(12.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Primary,
        contentColor = OnPrimary
      )
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(20.dp))
        Text(
          text = "Switch to AdaptiFi Accessible Flow",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
private fun BenchmarkFormField(
  label: String,
  value: String,
  issueBadge: String
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(SurfaceContainerLow)
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OnSurface)
      Box(
        modifier = Modifier
          .clip(CircleShape)
          .background(Error.copy(alpha = 0.12f))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      ) {
        Text("Barrier", color = Error, fontSize = 9.sp, fontWeight = FontWeight.Bold)
      }
    }
    Text(value, fontSize = 14.sp, color = OnSurfaceVariant)
    Text(issueBadge, fontSize = 11.sp, color = Error, fontWeight = FontWeight.Medium)
  }
}
