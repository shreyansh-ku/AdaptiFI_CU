package com.example.model

enum class ScreenTab {
  HOME,
  NAVIGATE,
  BAND,
  SETTINGS
}

data class AdaptationState(
  val adaptiveModeEnabled: Boolean = true,
  val activeAdaptationsCount: Int = 3,
  val sensorLatencyMs: Int = 18,
  val textSizeAdaptation: Boolean = true,
  val voiceInputAdaptation: Boolean = true,
  val simplifiedFormAdaptation: Boolean = true,
  val cognitiveLoadLow: Boolean = true,
  // Live simulated metrics (Demo mode)
  val ambientNoiseDb: Int = 52,
  val gazeDwellSec: Float = 1.4f,
  val handTremorHz: Float = 3.2f,
  val heartRateBpm: Int = 74,
  val crowdDensityPercent: Int = 42,
  val glareIndexPercent: Int = 30,
  // Barrier probabilities
  val visualBarrierProb: Int = 68,
  val acousticBarrierProb: Int = 34,
  val motorTremorBarrierProb: Int = 22,
  val cognitiveLoadBarrierProb: Int = 45
)

enum class BarrierStatus(val label: String) {
  DETECTED("Detected"),
  NOMINAL("Nominal"),
  STEADY("Steady"),
  MATCHED("Matched")
}

data class BarrierCategory(
  val id: String,
  val name: String,
  val iconName: String,
  val status: BarrierStatus,
  val isDetected: Boolean
)

data class RailwayBookingData(
  val passengerName: String = "Aarav Sharma",
  val identityProof: String = "XXXX-XXXX-4821 (Aadhaar)",
  val fromStation: String = "New Delhi (NDLS)",
  val toStation: String = "Chandigarh (CDG)",
  val travelDate: String = "15 Oct 2026",
  val trainNumber: String = "12045 / NDLS CDG Shatabdi",
  val coachSeat: String = "Coach C2 • Seat 12 (Lower Berth)",
  val pnrNumber: String = "248-9102847",
  val quota: String = "Divyangjan / Senior Citizen Priority"
)

enum class HapticPatternType {
  LEFT_TURN,
  RIGHT_TURN,
  STOP_HAZARD
}

data class ARWaypoint(
  val id: String,
  val instruction: String,
  val subtext: String,
  val distanceMeters: Int,
  val platformLabel: String,
  val pattern: HapticPatternType
)

data class AdaptationPipelineRule(
  val id: String,
  val condition: String,
  val adaptation: String,
  val triggerSensor: String,
  val isActive: Boolean = true
)

data class AppUserSettings(
  val fontScale: Float = 1.0f, // 1.0 to 1.6
  val highContrast: Boolean = false,
  val voicePromptsEnabled: Boolean = true,
  val hapticIntensity: Float = 0.8f, // 0.2 to 1.0
  val selectedLanguage: String = "EN" // "EN" or "HI"
)
