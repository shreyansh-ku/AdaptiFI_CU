package com.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.example.ui.screens.ARNavigationScreen
import com.example.ui.screens.AdaptedBookingScreen
import com.example.ui.screens.HapticBandScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SensingEngineScreen
import com.example.ui.screens.UnadaptedBookingScreen
import com.example.ui.theme.AdaptiFiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class AdaptiFiAppTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testHomeScreenButtonsAndToggles() {
    var calibratedClicked = false
    var bookingClicked = false
    var unadaptedClicked = false

    composeTestRule.setContent {
      AdaptiFiTheme {
        HomeScreen(
          onNavigateToBooking = { bookingClicked = true },
          onNavigateToUnadapted = { unadaptedClicked = true },
          onNavigateToCalibration = { calibratedClicked = true }
        )
      }
    }

    // Verify header and elements
    composeTestRule.onNodeWithText("Good afternoon, Alex").assertIsDisplayed()
    composeTestRule.onNodeWithTag("adaptive_master_card").assertIsDisplayed()

    // Test Calibrate button
    composeTestRule.onNodeWithTag("calibrate_button").assertIsDisplayed()
    composeTestRule.onNodeWithTag("calibrate_button").performClick()
    assert(calibratedClicked)

    // Test toggles
    composeTestRule.onNodeWithTag("toggle_text_size").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("toggle_text_size").performClick()

    composeTestRule.onNodeWithTag("toggle_voice_input").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("toggle_voice_input").performClick()

    composeTestRule.onNodeWithTag("toggle_simplified_form").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("toggle_simplified_form").performClick()

    // Test launch booking button
    composeTestRule.onNodeWithTag("launch_booking_btn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("launch_booking_btn").performClick()
    assert(bookingClicked)

    // Test Audit Form button
    composeTestRule.onNodeWithTag("history_button").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("history_button").performClick()
    assert(unadaptedClicked)
  }

  @Test
  fun testAdaptedBookingFlow() {
    var backClicked = false
    var unadaptedClicked = false

    composeTestRule.setContent {
      AdaptiFiTheme {
        AdaptedBookingScreen(
          onBack = { backClicked = true },
          onNavigateToUnadapted = { unadaptedClicked = true }
        )
      }
    }

    // Verify step 1
    composeTestRule.onNodeWithText("What's your name?").assertIsDisplayed()
    composeTestRule.onNodeWithTag("booking_next_button").performScrollTo().assertIsDisplayed()

    // Advance step 1 -> step 2
    composeTestRule.onNodeWithTag("booking_next_button").performClick()
    composeTestRule.onNodeWithText("National ID or Passport").assertIsDisplayed()

    // Advance step 2 -> step 3
    composeTestRule.onNodeWithTag("booking_next_button").performClick()
    composeTestRule.onNodeWithText("Departure & Destination").assertIsDisplayed()

    // Advance step 3 -> step 4
    composeTestRule.onNodeWithTag("booking_next_button").performClick()
    composeTestRule.onNodeWithText("Date & Seating").assertIsDisplayed()

    // Advance step 4 -> step 5 (Digital Boarding Pass)
    composeTestRule.onNodeWithTag("booking_next_button").performScrollTo().performClick()
    composeTestRule.onNodeWithText("Digital Boarding Pass Ready").performScrollTo().assertIsDisplayed()

    // Finish flow
    composeTestRule.onNodeWithTag("booking_next_button").performScrollTo().performClick()
    assert(backClicked)
  }

  @Test
  fun testHapticBandScreenButtons() {
    composeTestRule.setContent {
      AdaptiFiTheme {
        HapticBandScreen()
      }
    }

    composeTestRule.onNodeWithText("Haptic Calibration").performScrollTo().assertIsDisplayed()

    // Test Left Turn pattern button
    composeTestRule.onNodeWithTag("test_left_turn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("test_left_turn").performClick()

    // Test Right Turn pattern button
    composeTestRule.onNodeWithTag("test_right_turn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("test_right_turn").performClick()

    // Test Stop Hazard button
    composeTestRule.onNodeWithTag("test_hazard_button").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("test_hazard_button").performClick()

    // Test Disconnect Band button
    composeTestRule.onNodeWithTag("disconnect_band_btn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("disconnect_band_btn").performClick()
    composeTestRule.onNodeWithText("Reconnect Band").assertIsDisplayed()
  }

  @Test
  fun testSensingEngineScreenButtons() {
    var inspectBookingClicked = false

    composeTestRule.setContent {
      AdaptiFiTheme {
        SensingEngineScreen(
          onNavigateToBooking = { inspectBookingClicked = true }
        )
      }
    }

    // Refresh telemetry
    composeTestRule.onNodeWithTag("refresh_telemetry_btn").assertIsDisplayed()
    composeTestRule.onNodeWithTag("refresh_telemetry_btn").performClick()

    // Inspect pipeline rules
    composeTestRule.onNodeWithTag("inspect_pipeline_btn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("inspect_pipeline_btn").performClick()
    composeTestRule.onNodeWithText("Active Heuristic Pipeline Rules").performScrollTo().assertIsDisplayed()
  }

  @Test
  fun testUnadaptedBookingScreenButtons() {
    var backClicked = false
    var switchedToAdapted = false

    composeTestRule.setContent {
      AdaptiFiTheme {
        UnadaptedBookingScreen(
          onBack = { backClicked = true },
          onSwitchToAdapted = { switchedToAdapted = true }
        )
      }
    }

    composeTestRule.onNodeWithText("Ticket Booking").assertIsDisplayed()
    composeTestRule.onNodeWithTag("switch_to_adapted_btn").performScrollTo().assertIsDisplayed()
    composeTestRule.onNodeWithTag("switch_to_adapted_btn").performClick()
    assert(switchedToAdapted)
  }

  @Test
  fun testARNavigationControls() {
    var bandClicked = false

    composeTestRule.setContent {
      AdaptiFiTheme {
        ARNavigationScreen(
          onNavigateToBand = { bandClicked = true }
        )
      }
    }

    // Test contrast, flashlight, zoom toggles
    composeTestRule.onNodeWithTag("ar_contrast_toggle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ar_contrast_toggle").performClick()

    composeTestRule.onNodeWithTag("ar_flashlight_toggle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ar_flashlight_toggle").performClick()

    composeTestRule.onNodeWithTag("ar_zoom_toggle").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ar_zoom_toggle").performClick()

    // Test audio replay
    composeTestRule.onNodeWithTag("ar_replay_button").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ar_replay_button").performClick()
  }
}
