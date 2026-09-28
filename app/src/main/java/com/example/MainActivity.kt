package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.model.AdaptationState
import com.example.model.AppUserSettings
import com.example.model.ScreenTab
import com.example.ui.components.AdaptiFiBottomNavigation
import com.example.ui.components.AdaptiFiTopBar
import com.example.ui.screens.ARNavigationScreen
import com.example.ui.screens.AdaptedBookingScreen
import com.example.ui.screens.HapticBandScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SensingEngineScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.UnadaptedBookingScreen
import com.example.ui.theme.AdaptiFiTheme

enum class SubScreen {
  NONE,
  ADAPTED_BOOKING,
  UNADAPTED_BOOKING,
  SENSING_ENGINE
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MainAppRoot()
    }
  }
}

@Composable
fun MainAppRoot() {
  var userSettings by remember { mutableStateOf(AppUserSettings()) }

  AdaptiFiTheme(userSettings = userSettings) {
    MainAppContent(
      userSettings = userSettings,
      onUserSettingsChanged = { userSettings = it }
    )
  }
}

@Composable
fun MainAppContent(
  userSettings: AppUserSettings = AppUserSettings(),
  onUserSettingsChanged: (AppUserSettings) -> Unit = {}
) {
  var selectedTab by remember { mutableStateOf(ScreenTab.HOME) }
  var activeSubScreen by remember { mutableStateOf(SubScreen.NONE) }
  var adaptationState by remember { mutableStateOf(AdaptationState()) }

  // Handle system back navigation across secondary sub-screens
  BackHandler(enabled = activeSubScreen != SubScreen.NONE) {
    activeSubScreen = SubScreen.NONE
  }

  val subtitle = when {
    activeSubScreen == SubScreen.ADAPTED_BOOKING -> if (userSettings.selectedLanguage == "HI") "अनुकूली बुकिंग" else "Adapted Rail Booking"
    activeSubScreen == SubScreen.UNADAPTED_BOOKING -> if (userSettings.selectedLanguage == "HI") "पारंपरिक पोर्टल" else "Legacy Portal Audit"
    activeSubScreen == SubScreen.SENSING_ENGINE -> if (userSettings.selectedLanguage == "HI") "सेंसिंग इंजन" else "Sensing Engine"
    selectedTab == ScreenTab.HOME -> if (userSettings.selectedLanguage == "HI") "होम" else "Home"
    selectedTab == ScreenTab.NAVIGATE -> if (userSettings.selectedLanguage == "HI") "एआर नेविगेशन" else "Station AR Navigation"
    selectedTab == ScreenTab.BAND -> if (userSettings.selectedLanguage == "HI") "हैप्टिक बैंड" else "Haptic Band"
    selectedTab == ScreenTab.SETTINGS -> if (userSettings.selectedLanguage == "HI") "सेटिंग्स" else "Settings"
    else -> "Home"
  }

  Scaffold(
    topBar = {
      if (activeSubScreen == SubScreen.NONE) {
        AdaptiFiTopBar(
          subtitle = subtitle,
          onProfileClick = { selectedTab = ScreenTab.SETTINGS }
        )
      }
    },
    bottomBar = {
      if (activeSubScreen == SubScreen.NONE) {
        AdaptiFiBottomNavigation(
          currentTab = selectedTab,
          onTabSelected = { selectedTab = it }
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      AnimatedContent(
        targetState = Pair(selectedTab, activeSubScreen),
        transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
        label = "screen_transition"
      ) { target ->
        val (tab, sub) = target
        when {
          sub == SubScreen.ADAPTED_BOOKING -> {
            AdaptedBookingScreen(
              userSettings = userSettings,
              onBack = { activeSubScreen = SubScreen.NONE },
              onNavigateToUnadapted = { activeSubScreen = SubScreen.UNADAPTED_BOOKING }
            )
          }
          sub == SubScreen.UNADAPTED_BOOKING -> {
            UnadaptedBookingScreen(
              onBack = { activeSubScreen = SubScreen.NONE },
              onSwitchToAdapted = { activeSubScreen = SubScreen.ADAPTED_BOOKING }
            )
          }
          sub == SubScreen.SENSING_ENGINE -> {
            SensingEngineScreen(
              adaptationState = adaptationState,
              onAdaptationStateChanged = { adaptationState = it },
              onNavigateToBooking = { activeSubScreen = SubScreen.ADAPTED_BOOKING },
              onBack = { activeSubScreen = SubScreen.NONE }
            )
          }
          tab == ScreenTab.HOME -> {
            HomeScreen(
              userSettings = userSettings,
              onSettingsChanged = onUserSettingsChanged,
              onNavigateToBooking = { activeSubScreen = SubScreen.ADAPTED_BOOKING },
              onNavigateToUnadapted = { activeSubScreen = SubScreen.UNADAPTED_BOOKING },
              onNavigateToCalibration = { selectedTab = ScreenTab.BAND },
              onNavigateToSensingEngine = { activeSubScreen = SubScreen.SENSING_ENGINE }
            )
          }
          tab == ScreenTab.NAVIGATE -> {
            ARNavigationScreen(
              onNavigateToBand = { selectedTab = ScreenTab.BAND }
            )
          }
          tab == ScreenTab.BAND -> {
            HapticBandScreen()
          }
          tab == ScreenTab.SETTINGS -> {
            SettingsScreen(
              userSettings = userSettings,
              onSettingsChanged = onUserSettingsChanged,
              onResetDemo = {
                userSettings = AppUserSettings()
                adaptationState = AdaptationState()
              },
              onOpenSensingEngine = { activeSubScreen = SubScreen.SENSING_ENGINE }
            )
          }
        }
      }
    }
  }
}
