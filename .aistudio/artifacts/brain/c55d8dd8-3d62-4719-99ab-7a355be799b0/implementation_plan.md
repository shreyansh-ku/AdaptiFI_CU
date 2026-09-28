# Implementation Plan: AdaptiFi — Ambient Accessibility & Sensory Intelligence

AdaptiFi is an ambient assistive Android application that dynamically adapts interfaces to cognitive, sensory, and motor needs. Based on the design specifications and user preferences, this plan details the architecture and features to reproduce the exact screens, typography, color palette, and interactive feedback.

---

## 1. Design System & Theming
- **Color Palette**:
  - Primary: `#4648D4` (Digital Indigo) & `#6063EE` (Container)
  - Secondary: `#006C49` & `#6CF8BB` / `#6FFBBE` (Emerald Accent & Connected states)
  - Tertiary: `#825100` & `#FFDDB8` / `#FFB95F` (Amber / Warning / Hazard)
  - Surface: `#F8F9FF`, `#FFFFFF` (Lowest container), `#EFF4FF` (Low container), `#DCE9FF` (High container)
  - State: `#10B981` (Success), `#F59E0B` (Warning), `#EF4444` (Danger)
- **Typography**:
  - Display & Headers: Bold geometric modern styling (Manrope-equivalent weights: ExtraBold 800, Bold 700)
  - Labels & Body: Hyper-legible clean grotesque (Hanken Grotesk-equivalent weights) with explicit tracking and line heights
- **Touch & Accessibility Standards**:
  - Minimum 56dp ergonomic touch targets for interactive controls
  - WCAG AAA high-contrast pairings
  - Visual status pulses, active pills, and high-visibility typography

---

## 2. Core App Architecture & Navigation
- **Navigation Model**:
  - Bottom Navigation Bar with 4 primary destinations:
    1. **Home**: Environmental intelligence, barrier overview, live adaptations, cognitive load status, quick mic FAB.
    2. **Navigate (AR View)**: First-person concourse view, 3D directional arrows, detected signage bounding reticle, compass HUD, contrast/zoom tools, and audio/haptic turn cues.
    3. **Band (Haptic Band)**: BLE link schematic, live battery telemetry, interactive sensory calibration (LEFT Turn, RIGHT Turn, STOP/Hazard) with device vibration and audio chimes.
    4. **Settings (Sensing Engine)**: Telemetry diagnostics, camera/mic/interaction dynamic streams, real-time barrier probability meters (Visual 78%, Motor 22%, Cognitive 41%), and offline edge engine pipeline inspector.
  - Dedicated Flow Screens:
    - **Adapted Booking (5-Step Flow)**: Step-by-step cognitive-friendly booking (Passenger Details, Seat Selection, Baggage, Assistance, Review) with voice dictation, quick chips, and serene reassurance card.
    - **Unadapted Booking Comparison**: Side-by-side diagnostic mode displaying the legacy cramped form with 8 detected accessibility barriers, low contrast warning, and a direct switch button to AdaptiFi mode.

---

## 3. Sensory Feedback & Hardware Integration
- **Haptic Vibration Engine**:
  - Use Android `Vibrator` / `VibratorManager` with `VibrationEffect` compositions:
    - *Left Turn*: 2 short crisp buzzes on left channel (120ms - 80ms - 120ms).
    - *Right Turn*: 2 short crisp buzzes on right channel (120ms - 80ms - 120ms).
    - *STOP / Hazard*: Continuous intense long pulse (650ms urgent buzz).
- **Audio Chime Generator**:
  - Implement synthesized audio cues via `AudioTrack` / `ToneGenerator` for auditory confirmation when calibration tests or voice inputs trigger.
- **Microphone & Speech Dictation**:
  - Interactive speech overlay with animated listening waves and quick voice-to-text autofill simulation.

---

## 4. Key Screens & Features

### Screen 1: Home Dashboard (`HomeScreen.kt`)
- Header: AdaptiFi branding, "SENSING" live pulse pill, user profile avatar.
- Greeting: "Environmental Intelligence - Good afternoon, Alex" with routine icon.
- Master Status Card: "Adaptive mode: ON", sensor latency 18ms, zero telemetry shared indicator, micro-meter.
- Barrier Detection horizontal carousel: Visual (Detected), Hearing (Nominal), Motor (Steady), Cognitive (Detected), Language (Matched).
- Live Adaptations list with interactive toggles:
  - *Text size increased* (+40% scale applied).
  - *Voice input enabled* (Dictation overlay ready).
  - *Simplified form* (Multi-step breakdown active).
- Ambient Delight Card: "Cognitive load is low" with details trigger.
- Voice Assistant FAB: 64dp glowing pulse button with audio interaction state.

### Screen 2: AR Navigation (`ArNavigationScreen.kt`)
- Live simulated station concourse backdrop with calibrated ground grid.
- Top HUD: AR LIVE indicator, "40m to Platform 3" distance badge, connected haptic wristband chip.
- Floating Signage Detection Reticle: Neon corner accents, high-confidence badge, "PLATFORM 3" tracking.
- Hero 3D Turn Chevron: Glowing animated turn-left arrow.
- Orientation Compass: Real-time heading needle (NW 312°).
- Assistive Camera Tools: High-contrast toggle, navigation flashlight toggle, 2× magnification zoom.
- Bottom Floating Command Card: Microphone pulse trigger, "Turn left to Platform 3" instruction, "Haptic pulse sent to left wrist" confirmation, and Replay Audio Guidance button.
- Interactive Haptic Toast: Floating animated toast confirming haptic cadence delivery.

### Screen 3: Haptic Band (`HapticBandScreen.kt`)
- Hardware Link Status: Connected, 92% battery, BLE 5.3 encryption notice.
- Interactive Vector Schematic: Phone to wristband live animated transmission wave with bluetooth link.
- Sensory Philosophy Banner: "Phone = intelligence, Band = private, immediate feedback".
- Calibration Suite with Test buttons:
  - LEFT Turn: 2 short buzzes + device vibration + audio tone.
  - RIGHT Turn: 2 short buzzes + device vibration + audio tone.
  - STOP / Hazard: Urgent long pulse + amber highlight + strong device vibration.
- Disconnect / Reconnect action with animated state transitions.

### Screen 4: Sensing Engine (`SensingEngineScreen.kt`)
- Telemetry Diagnostic Banner: Edge perception model v4.8 active + refresh metrics trigger.
- Input Feed Streams:
  - Camera: Live 30fps, gaze vector locked, pupil dilation telemetry.
  - Microphone: Real-time animated audio equalizer bars, 48 dB cafe hum detection.
  - Interaction Dynamics: Alert badge, hesitation & tremor detected, target miss counter.
- Real-time Barrier Probability:
  - Visual Barrier: 78% (squinting, low contrast).
  - Motor Barrier: 22% (stable grip telemetry).
  - Cognitive Barrier: 41% (field re-reads, typography sensory overload).
- Adaptation Engine Card: Micro-heuristic active transform, rule trigger callout, inspect pipeline button, on-device privacy guarantee.

### Screen 5: 5-Step Adapted Booking (`AdaptedBookingScreen.kt`)
- Progress Stepper: Step 1 of 5 (Passenger Details) with high-visibility indicator bars.
- Focus Flight Card: Flight AF-804 (San Francisco to Seattle, Gate B12) with airplane wing imagery.
- Ergonomic Question Card:
  - "What's your name?" headline with high-contrast text.
  - 56dp input field with integrated high-contrast microphone button.
  - Speech dictation overlay with live listening badge.
  - Quick-fill traveler profile chips: Elena Vance-Wu, Dr. Marcus Vance.
- Primary NEXT button with saving micro-interaction and 5-step progression.
- AdaptiFi Calm Assurance Card: Large text, Voice input, One step at a time badges.

### Screen 6: Unadapted Legacy Booking Comparison (`UnadaptedBookingScreen.kt`)
- Diagnostic Alert Banner: "Unadapted View • 8 Form Barriers Detected • Audit Active".
- Cramped Inaccessible Form: Low-contrast inputs, 28px tiny touch targets, microscopic labels, strict formats.
- Barrier Callout Indicators: Visualizing where motor and cognitive friction occurs.
- Comparative Value Card: "Why AdaptiFi Re-architects This" with 56px target and voice engine highlights.
- Seamless "View in AdaptiFi Accessible Mode" toggle button.

---

## 5. Verification & Polishing
- Build verification using `compile_applet`.
- Adaptive custom app icon setup matching the AdaptiFi brand identity.
- Clean edge-to-edge window insets handling for top status bar and bottom navigation bar.
- Tested touch targets meeting or exceeding 56dp for effortless motor accessibility.
