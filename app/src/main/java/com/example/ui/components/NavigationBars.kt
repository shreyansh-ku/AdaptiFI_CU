package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurface
import com.example.ui.theme.Primary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StateSuccess
import com.example.ui.theme.Surface
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.TextMuted

@Composable
fun AdaptiFiTopBar(
  subtitle: String,
  onProfileClick: () -> Unit = {}
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = Surface.copy(alpha = 0.95f),
    shadowElevation = 2.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Brand Logo & Text Hierarchy
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // App icon badge
        Box(
          modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Primary),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "A",
            color = OnPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
          )
        }

        Column {
          Text(
            text = "AdaptiFi",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = OnSurface,
            letterSpacing = (-0.5).sp
          )
          Text(
            text = subtitle,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextMuted
          )
        }
      }

      // Sensing Indicator & Profile Avatar
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Pill: SENSING (Demo Mode: Simulated Sensors)
        Row(
          modifier = Modifier
            .clip(CircleShape)
            .background(SecondaryContainer.copy(alpha = 0.5f))
            .padding(horizontal = 9.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Box(
            modifier = Modifier
              .size(7.dp)
              .clip(CircleShape)
              .background(StateSuccess)
          )
          Text(
            text = "SIMULATED",
            color = OnSecondaryContainer,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
          )
        }

        // Profile icon button
        IconButton(
          onClick = onProfileClick,
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Primary)
            .testTag("profile_button")
        ) {
          Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User profile",
            tint = OnPrimary,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}

@Composable
fun AdaptiFiBottomNavigation(
  currentTab: ScreenTab,
  onTabSelected: (ScreenTab) -> Unit
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = Surface.copy(alpha = 0.95f),
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(68.dp)
        .padding(horizontal = 8.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      NavigationItem(
        label = "Home",
        icon = Icons.Default.Home,
        selected = currentTab == ScreenTab.HOME,
        onClick = { onTabSelected(ScreenTab.HOME) },
        testTag = "nav_home"
      )
      NavigationItem(
        label = "Navigate",
        icon = Icons.Default.Explore,
        selected = currentTab == ScreenTab.NAVIGATE,
        onClick = { onTabSelected(ScreenTab.NAVIGATE) },
        testTag = "nav_navigate"
      )
      NavigationItem(
        label = "Band",
        icon = Icons.Default.Watch,
        selected = currentTab == ScreenTab.BAND,
        onClick = { onTabSelected(ScreenTab.BAND) },
        testTag = "nav_band"
      )
      NavigationItem(
        label = "Settings",
        icon = Icons.Default.Tune,
        selected = currentTab == ScreenTab.SETTINGS,
        onClick = { onTabSelected(ScreenTab.SETTINGS) },
        testTag = "nav_settings"
      )
    }
  }
}

@Composable
private fun NavigationItem(
  label: String,
  icon: ImageVector,
  selected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  val interactionSource = remember { MutableInteractionSource() }

  Column(
    modifier = Modifier
      .clip(RoundedCornerShape(12.dp))
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .padding(horizontal = 14.dp, vertical = 8.dp)
      .testTag(testTag),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(2.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = label,
      tint = if (selected) Primary else TextMuted,
      modifier = Modifier.size(24.dp)
    )
    Text(
      text = label,
      color = if (selected) Primary else TextMuted,
      fontSize = 12.sp,
      fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
    )
  }
}
