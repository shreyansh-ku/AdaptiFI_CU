# AdaptiFi — Comprehensive Technical & Product Specification

## 1. Executive Summary & Value Proposition

**AdaptiFi** is a next-generation assistive mobile platform built for Android. It bridges the physical, sensory, cognitive, and digital accessibility gaps encountered in high-stress transit environments (such as international airports, subterranean metro stations, and central rail terminals).

### Core Problem
1. **Digital Bottlenecks:** Mainstream transit booking engines and airline apps present high cognitive friction: dense unsegmented forms, illegible low-contrast text (often < 2:1), microscopic hit targets (28–32px), confusing abbreviations, and punitive session expiration timers.
2. **Physical Overload in Transit:** Navigating crowded hubs with sensory disabilities, cognitive fatigue, visual impairment, or motor tremors while managing luggage or mobility aids leads to disorientation, missed connections, and collision risks.
3. **Screen Distraction:** Traditional GPS applications force users to keep their eyes fixed on a screen rather than observing real-world physical surroundings.

### The AdaptiFi Solution
- **Autonomous Real-Time UI Adaptation:** On-device classifiers evaluate sensory telemetry (viewing distance, dwell time, ambient noise, touch tremor) to adapt UI scaling (+40%), activate speech overlays, and simplify complex screens into bite-sized steps.
- **Augmented Reality Spatial Wayfinding:** Heads-up 3D station corridor perspective with high-visibility ground chevrons, neon signage bounding reticles (`PLATFORM 3 / GATE B12`), and interactive waypoint advancing.
- **Eyes-Free Wrist Haptic Direction:** Directional navigation offloaded to a paired haptic band utilizing distinct tactile pulses (dual-pulse left turn, crisp directional right cadence, and continuous urgent stop hazard).
- **100% On-Device Privacy Architecture:** Zero telemetry is transmitted externally. Sensory classification runs entirely in-memory on the edge.

---

## 2. Technical Stack & Engineering Specifications

| Dimension | Specification |
| :--- | :--- |
| **Platform / OS** | Android 14+ (minSdk 24, compileSdk 36, targetSdk 36) |
| **Language** | Kotlin 100% (Strict Null-Safety, Coroutines, StateFlow) |
| **UI Framework** | Jetpack Compose (Declarative UI, Material Design 3) |
| **Theming System** | Custom High-Contrast Health/Transit Palette (`Theme.kt`, `Color.kt`) |
| **Accessibility Compliance** | WCAG 2.2 Level AAA (48dp+ interactive targets, 4.5:1 to 7:1 contrast) |
| **Hardware Integrations** | `android.os.Vibrator`, `VibrationEffect`, `ToneGenerator`, `AudioManager` |
| **Testing Architecture** | Local JVM Robolectric 4.14+ with Compose Test Rule (`AdaptiFiAppTest.kt`) |
| **Build Tooling** | Gradle Kotlin DSL (`build.gradle.kts`) with Version Catalog |

---

## 3. System Architecture & Component Hierarchy

```
                             [MainActivity]
                                   │
               ┌───────────────────┴───────────────────┐
               ▼                                       ▼
       [TopStatusBar]                         [BottomNavigationBar]
    • Real-time Sensing Badge              • Home Tab (HOME)
    • Profile & Telemetry Latency          • Navigate Tab (NAVIGATE)
                                           • Band Tab (BAND)
                                           • Settings Tab (SETTINGS)
                                   │
       ┌───────────────────────────┼───────────────────────────┐
       ▼                           ▼                           ▼
  [HomeScreen]           [AdaptedBookingScreen]     [UnadaptedBookingScreen]
• Adaptive Master Card   • Step 1: Passenger Name   • 8-Barrier Inaccessible
• Barrier Carousel       • Step 2: Travel Passport    Audit Benchmark
• 3 Active Toggles       • Step 3: Route Terminals  • 28px Tap Targets
• Quick Booking Card     • Step 4: Seating Coach    • Low Contrast (1.8:1)
• Floating Mic FAB       • Step 5: Digital Boarding • 1-Tap Switch Back Button
                           Pass with Barcode
       │
       ├───────────────────────────┬───────────────────────────┐
       ▼                           ▼                           ▼
[ARNavigationScreen]      [HapticBandScreen]         [SensingEngineScreen]
• Perspective Corridors  • Phone-to-Band Data Flow   • 4 Sensor Telemetry Meters
• Neon Reticle Signage   • Left Turn Calibration     • Barrier Probabilities
• Directional Chevrons   • Right Turn Calibration    • Heuristic Rules Engine
• Waypoint Distance Sync • Stop Hazard Calibration   • One-Tap Pipeline Rules
• Night Torch & Zoom     • BLE 5.3 Link Toggle         Inspector Modal
```

---

## 4. Complete Screen Directory & User Journeys

### 4.1 `HomeScreen.kt` (Adaptive Command Center)
- **Top Greeting & Telemetry Master Card:**
  - Displays user status (*Good afternoon, Alex*).
  - High-visibility status card showing sensor latency (*18ms*), background adaptation state, and micro-telemetry meter.
  - **Calibrate Shortcut:** Direct button that navigates into the haptic band suite.
- **Barrier Detection Horizontal Carousel:**
  - Categorizes 5 situational conditions:
    1. *Visual Strain / Glare* (Detected — nominal ambient sunlight)
    2. *Auditory Crowding* (Detected — 72 dB station noise)
    3. *Motor Tremor / Instability* (Steady — 12ms touch cadence)
    4. *Cognitive Overload* (Nominal — short dwell patterns)
    5. *Language Context* (Matched — English UK active)
- **Live Adaptation Toggles:**
  - *Text Size Increased:* Scales typography by +40% dynamically across app layouts.
  - *Voice Input Enabled:* Enables dictation overlay with acoustic noise suppression.
  - *Simplified Form Flow:* Enforces chunked multi-step decomposition.
- **Interactive Booking Card & Form Audit Link:**
  - One-tap launcher for the accessible 5-step booking flow.
  - "Audit Form" link to inspect the inaccessible benchmark.
- **Voice Assistant Floating Action Button (FAB):**
  - 64dp high-contrast circular button with real-time listening animation and acoustic audio confirmation.

### 4.2 `AdaptedBookingScreen.kt` (5-Step Frictionless Booking)
- **Design Philosophy:** Single-intent focus per step to minimize cognitive load.
- **Step Breakdown:**
  1. *Passenger Name:* Large touch target text input with quick-fill chips (*Elena Vance-Wu*, *Dr. Marcus Vance*) and voice dictation button.
  2. *Travel ID / Passport:* Autofilled biometric travel document with visual verification shield.
  3. *Terminals:* Departure (*San Francisco SFO*) and Destination (*Seattle SEA*) with plain-language terminal indicators.
  4. *Date & Seating:* High-contrast date selector and coach seating (*12A Quiet Zone*).
  5. *Verified Digital Boarding Pass:* Complete flight pass display with passenger identity, seat coach, gate status (*B12 On Time*), and on-device synchronized barcode bar. Concludes with a completion button returning safely to the home view.

### 4.3 `UnadaptedBookingScreen.kt` (Accessibility Benchmark & Audit)
- Serves as the benchmark showing the contrast between traditional digital barriers and AdaptiFi.
- Explicitly exposes 8 friction bottlenecks:
  1. Microscopic 28px tap targets causing mis-taps.
  2. Severe low contrast (1.8:1 ratio) unreadable in direct sunlight.
  3. Cryptic non-standard airport codes with missing labels.
  4. Stressful countdown timer (*04:12 remaining*) inducing cognitive anxiety.
  5. Dense multi-field clustering without progressive disclosure.
  6. Absence of screen-reader accessible labeling.
  7. No voice or speech assistance affordances.
  8. Missing hardware haptic feedback cues.
- Features a prominent top callout banner: *"Experiencing friction? Switch to AdaptiFi's Accessible Mode"*.

### 4.4 `ARNavigationScreen.kt` (Augmented Reality Wayfinding)
- **Station HUD Viewport:** Simulated first-person perspective with converging floor perspective lines and animated 3D directional chevrons.
- **Neon Signage Reticle Detection:** Dynamic green bounding reticle locking onto station overhead signs (*PLATFORM 3 / GATE B12*).
- **Interactive Multi-Waypoint Route Guidance:**
  - *Waypoint 1:* Turn left to Platform 3 (40m, left wrist haptic pulse)
  - *Waypoint 2:* Continue straight through Concourse B (25m, gentle double cadence)
  - *Waypoint 3:* Caution: Approaching Staircase & Ramp (8m, urgent stop alert)
  - *Waypoint 4:* Arrived at Gate B12 Priority Lane (0m, boarding door open)
  - Tapping the navigation card advances waypoints sequentially while triggering synchronized device vibrations and audio feedback.
- **HUD Utility Controls:**
  - High Contrast Toggle (switches to black/yellow contrast mode).
  - Night Flashlight Torch (dark terminal corridor lighting overlay).
  - 2× Magnification Toggle (zooms signage reticle).
  - Replay Audio Cue button.

### 4.5 `HapticBandScreen.kt` (Wristband Hardware Calibration)
- **Wearable Architecture Schematic:** Explains the decoupled model: *Phone = Sensor Intelligence, Band = Private Immediate Feedback*.
- **Tactile Pattern Calibration Suite:**
  - *LEFT Turn:* 2 short pulses with high-frequency pitch.
  - *RIGHT Turn:* 2 crisp directional pulses with double chime.
  - *STOP / Hazard:* Continuous urgent long vibration with warning tone.
- **BLE Connection Manager:** Disconnect and reconnect BLE 5.3 hardware link with status updates.

### 4.6 `SensingEngineScreen.kt` (Telemetry Diagnostics & Heuristic Rules)
- **Live Sensor Telemetry:**
  - Gaze Dwell Time (*2.1s — Normal focus*)
  - Ambient Noise Level (*64 dB — Terminal chatter*)
  - Touch Tremor Index (*14% — Stable interaction*)
  - Heart Rate Variability (*71 bpm — Calm transit state*)
- **Barrier Probability Matrix:** Real-time computation of sensory and cognitive barrier risks.
- **Active Heuristic Rules Inspector:** Expandable matrix displaying on-device transformation rules:
  1. `Gaze Dwell > 2.8s` $\rightarrow$ Trigger Form Decomposition (3–5 bite-sized chunks).
  2. `Ambient Noise > 55 dB` $\rightarrow$ Enable Acoustic Isolation & Visual Subtitles.
  3. `Touch Tremor / Mis-taps` $\rightarrow$ Expand Touch Target Hitboxes by +50% with 200ms debounce.
  4. `Distance < 20m from Waypoint` $\rightarrow$ Increase Haptic Cadence inversely proportional to distance.

---

## 5. Data Models & State Architecture (`AppModels.kt`)

```kotlin
enum class ScreenTab { HOME, NAVIGATE, BAND, SETTINGS }

data class AdaptationState(
  val adaptiveModeEnabled: Boolean = true,
  val activeAdaptationsCount: Int = 3,
  val sensorLatencyMs: Int = 18,
  val textSizeAdaptation: Boolean = true,
  val voiceInputAdaptation: Boolean = true,
  val simplifiedFormAdaptation: Boolean = true,
  val cognitiveLoadLow: Boolean = true
)

enum class HapticPatternType { LEFT_TURN, RIGHT_TURN, STOP_HAZARD }

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
```

---

## 6. Design System & Accessibility Standards

- **Touch Targets:** All primary buttons and interactive cards strictly enforce $\ge 56\text{dp}$ touch targets with `testTag` modifiers for accessibility and automated testing.
- **Color Contrast:** Deep navy (`0xFF0B1C30`) text over high-luminance light surface (`0xFFFFFFFF` / `0xFFF8F9FF`), achieving WCAG AAA contrast ratio exceeding 7:1.
- **Haptic & Acoustic Fallbacks:** Every visual event (turn, arrival, error, warning) is paired with a distinct tactile vibration and an audio tone to ensure multisensory redundancy.

---

## 7. Verification & Automated Test Suite

The test suite in `/app/src/test/java/com/example/AdaptiFiAppTest.kt` executes via Robolectric on Android SDK 36:
- `testHomeScreenButtonsAndToggles`: Validates master card, calibrate shortcut, toggles, booking button, and audit button.
- `testAdaptedBookingFlow`: Validates progression through all 5 steps up to digital boarding pass generation.
- `testHapticBandScreenButtons`: Validates Left, Right, Stop hazard patterns, and BLE connection toggles.
- `testSensingEngineScreenButtons`: Validates telemetry refresh and pipeline rules inspection.
- `testUnadaptedBookingScreenButtons`: Validates audit screen and switch-to-adapted button.
- `testARNavigationControls`: Validates contrast, flashlight, zoom, and audio replay controls.
