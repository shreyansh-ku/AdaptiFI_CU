package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppUserSettings
import com.example.model.HapticPatternType
import com.example.model.RailwayBookingData
import com.example.ui.theme.InverseOnSurface
import com.example.ui.theme.InverseSurface
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSecondaryFixed
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
import com.example.ui.theme.StateSuccess
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
fun AdaptedBookingScreen(
  userSettings: AppUserSettings = AppUserSettings(),
  onBack: () -> Unit,
  onNavigateToUnadapted: () -> Unit
) {
  val context = LocalContext.current
  val isHindi = userSettings.selectedLanguage == "HI"

  var currentStep by remember { mutableIntStateOf(1) }
  var bookingData by remember { mutableStateOf(RailwayBookingData()) }
  var isDictating by remember { mutableStateOf(false) }
  var showHelpDialog by remember { mutableStateOf(false) }

  // Animated pulse for active voice/recording indication
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
  val pulseAlpha by infiniteTransition.animateFloat(
    initialValue = 0.3f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_alpha"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Surface)
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. Top Navigation Bar
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
          onClick = {
            if (currentStep > 1) {
              currentStep--
              HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.LEFT_TURN)
            } else {
              onBack()
            }
          },
          modifier = Modifier.testTag("booking_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Primary
          )
        }

        Column {
          Text(
            text = if (isHindi) "भारतीय रेल टिकट बुकिंग" else "Railway Ticket Booking",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = OnSurface
          )
          Text(
            text = if (isHindi) "चरण $currentStep / 5 • सुलभ सहायता" else "Step $currentStep of 5 • Assisted Flow",
            fontSize = 12.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Surface(
        shape = RoundedCornerShape(20.dp),
        color = SurfaceContainerHigh,
        modifier = Modifier.clickable { onNavigateToUnadapted() }
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.SwapHoriz,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = if (isHindi) "पारंपरिक ऑडिट" else "Audit View",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
          )
        }
      }
    }

    // 2. Segmented Step Progress Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp),
      horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      for (step in 1..5) {
        val isFinished = step < currentStep
        val isCurrent = step == currentStep
        val barColor = when {
          isFinished -> StateSuccess
          isCurrent -> Primary
          else -> SurfaceContainerHigh
        }

        Box(
          modifier = Modifier
            .weight(1f)
            .height(6.dp)
            .clip(CircleShape)
            .background(barColor)
        )
      }
    }

    // 3. Main Step Card Container (Single intent per step)
    Surface(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      color = SurfaceContainerLowest,
      shadowElevation = 2.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Step Banner
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(PrimaryFixed.copy(alpha = 0.5f))
            .padding(horizontal = 14.dp, vertical = 10.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(Primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "$currentStep",
              color = OnPrimary,
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Text(
            text = when (currentStep) {
              1 -> if (isHindi) "यात्री विवरण (नाम)" else "Passenger Details"
              2 -> if (isHindi) "पहचान पत्र (आधार / पासपोर्ट)" else "Identity Proof Verification"
              3 -> if (isHindi) "मार्ग चयन (प्रस्थान एवं गंतव्य)" else "Route & Train Selection"
              4 -> if (isHindi) "यात्रा तिथि एवं कोच सीट" else "Date & Coach Preference"
              else -> if (isHindi) "सत्यापित डिजिटल टिकट व पीएनआर" else "Verified Railway E-Ticket"
            },
            color = Primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }

        // Title and description
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = when (currentStep) {
              1 -> if (isHindi) "आपका पूरा नाम क्या है?" else "What's your name?"
              2 -> if (isHindi) "पहचान प्रमाण (आधार / पहचान पत्र)" else "National ID or Passport"
              3 -> if (isHindi) "प्रस्थान एवं गंतव्य स्टेशन" else "Departure & Destination"
              4 -> if (isHindi) "यात्रा तिथि एवं कोच बर्थ" else "Date & Seating"
              else -> if (isHindi) "डिजिटल ई-टिकट तैयार है" else "Digital Boarding Pass Ready"
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = OnSurface,
            letterSpacing = (-0.5).sp
          )
          Text(
            text = when (currentStep) {
              1 -> if (isHindi) "रेलवे आरक्षण के लिए सरकारी पहचान पत्र वाला नाम लिखें।" else "Enter your legal name as shown on your travel identity proof."
              2 -> if (isHindi) "सुरक्षित रूप से मास्क किया गया 12-अंकीय आधार या पासपोर्ट।" else "Provide your masked Aadhaar or national identity number."
              3 -> if (isHindi) "नई दिल्ली (NDLS) से चंडीगढ़ (CDG) शताब्दी एक्सप्रेस।" else "Select your origin station and destination station."
              4 -> if (isHindi) "कोच C2, लोअर बर्थ, दिव्यांगजन / वरिष्ठ नागरिक प्राथमिकता।" else "Review your travel date and preferred train coach."
              else -> if (isHindi) "पीएनआर नंबर 248-9102847 जनरेट हो चुका है।" else "Confirm all railway details. Haptic wrist guidance is ready."
            },
            fontSize = 14.sp,
            color = OnSurfaceVariant,
            lineHeight = 20.sp
          )
        }

        // Dynamic Form Input for Current Step
        when (currentStep) {
          1 -> {
            // STEP 1: Name
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text(
                text = if (isHindi) "यात्री का पूरा नाम" else "Full Passenger Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
              )
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
              ) {
                OutlinedTextField(
                  value = bookingData.passengerName,
                  onValueChange = { bookingData = bookingData.copy(passengerName = it) },
                  modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .testTag("input_full_name"),
                  shape = RoundedCornerShape(12.dp),
                  placeholder = { Text(if (isHindi) "नाम टाइप करें या बोलें..." else "Tap or speak name...") },
                  colors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    focusedIndicatorColor = Primary,
                    unfocusedIndicatorColor = OutlineVariant
                  ),
                  trailingIcon = {
                    Box(
                      modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isDictating) Primary else SurfaceContainerHigh)
                        .clickable {
                          isDictating = !isDictating
                          HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
                          if (isDictating) {
                            bookingData = bookingData.copy(passengerName = "Aarav Sharma")
                          }
                        }
                        .testTag("dictate_name_button"),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Dictate name",
                        tint = if (isDictating) OnPrimary else Primary,
                        modifier = Modifier.size(20.dp)
                      )
                    }
                  }
                )
              }

              // Fast profile chips
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = SurfaceContainerLow,
                  modifier = Modifier.clickable {
                    bookingData = bookingData.copy(passengerName = "Aarav Sharma")
                    HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
                  }
                ) {
                  Text(
                    text = "Aarav Sharma (Self)",
                    color = Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(20.dp),
                  color = SurfaceContainerLow,
                  modifier = Modifier.clickable {
                    bookingData = bookingData.copy(passengerName = "Dr. Sunita Sharma")
                    HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
                  }
                ) {
                  Text(
                    text = "Dr. Sunita Sharma",
                    color = Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                  )
                }
              }
            }
          }

          2 -> {
            // STEP 2: Identity Proof
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
              Text(
                text = if (isHindi) "पहचान प्रमाण पत्र" else "Identity Proof (Masked)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
              )
              OutlinedTextField(
                value = bookingData.identityProof,
                onValueChange = { bookingData = bookingData.copy(identityProof = it) },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(60.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                  focusedContainerColor = SurfaceContainerLow,
                  unfocusedContainerColor = SurfaceContainerLow,
                  focusedIndicatorColor = Primary,
                  unfocusedIndicatorColor = OutlineVariant
                ),
                leadingIcon = {
                  Icon(Icons.Default.Badge, contentDescription = null, tint = Primary)
                }
              )

              Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceContainerLow,
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(
                  modifier = Modifier.padding(12.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = StateSuccess, modifier = Modifier.size(18.dp))
                  Text(
                    text = if (isHindi) "आधार डेटा एन्क्रिप्टेड है एवं केवल टीटीई सत्यापन हेतु मान्य है।" else "Aadhaar UIDAI token encrypted for contactless TTE verification.",
                    fontSize = 12.sp,
                    color = TextMuted
                  )
                }
              }
            }
          }

          3 -> {
            // STEP 3: Route & Train
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Text(
                text = if (isHindi) "ट्रेन एवं स्टेशन मार्ग" else "Train & Route",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
              )

              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLow
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Column {
                      Text("FROM", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.fromStation, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                    }
                    Icon(Icons.Default.Train, contentDescription = null, tint = Primary, modifier = Modifier.size(24.dp))
                    Column(horizontalAlignment = Alignment.End) {
                      Text("TO", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.toStation, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = OnSurface)
                    }
                  }

                  Text(
                    text = "Train: ${bookingData.trainNumber}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                  )
                }
              }
            }
          }

          4 -> {
            // STEP 4: Date & Coach
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Text(
                text = if (isHindi) "कोच एवं बर्थ प्राथमिकता" else "Coach & Berth Preference",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurface
              )

              Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = SurfaceContainerLow
              ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Column {
                      Text("DATE", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.travelDate, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                      Text("SEAT ALLOCATION", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.coachSeat, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Primary)
                    }
                  }

                  Text(
                    text = "Priority Quota: ${bookingData.quota}",
                    fontSize = 12.sp,
                    color = Secondary,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }

          5 -> {
            // STEP 5: Digital E-Ticket & PNR
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(StateSuccess),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = OnPrimary, modifier = Modifier.size(18.dp))
                }
                Text(
                  text = if (isHindi) "ई-टिकट एवं पीएनआर तैयार है" else "Digital Boarding Pass Ready",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.Bold,
                  color = Primary
                )
              }

              Surface(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(14.dp)),
                color = SurfaceContainerLow,
                shadowElevation = 1.dp
              ) {
                Column(
                  modifier = Modifier.padding(16.dp),
                  verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Column {
                      Text("PASSENGER", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.passengerName, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                      Text("PNR NUMBER", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text(bookingData.pnrNumber, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = Primary)
                    }
                  }

                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Column {
                      Text("TRAIN", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text("12045 Shatabdi", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Column {
                      Text("PLATFORM", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text("Platform 3", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Tertiary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                      Text("COACH / BERTH", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                      Text("C2 / 12 Lower", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Secondary)
                    }
                  }

                  // Simulated QR-style Code for TTE contactless scanning
                  Row(
                    modifier = Modifier
                      .fillMaxWidth()
                      .height(32.dp)
                      .clip(RoundedCornerShape(6.dp))
                      .background(OnSurface)
                      .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Row(
                      verticalAlignment = Alignment.CenterVertically,
                      horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                      Icon(Icons.Default.QrCode2, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                      Text(
                        text = "IRCTC • CONTACTLESS VERIFIED",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                      )
                    }
                    Text(
                      text = "SYNCED",
                      color = SecondaryFixed,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Bold
                    )
                  }
                }
              }

              Text(
                text = if (isHindi)
                  "हैप्टिक रिस्टबैंड पर प्लेटफॉर्म 3 व कोच C2 के लिए कंपन मार्गदर्शन तैयार है।"
                else
                  "Haptic wristband turn guidance synced for Platform 3 & Coach C2 boarding.",
                fontSize = 13.sp,
                color = Secondary,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }
    }

    // 4. Primary Advance Action Button
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Button(
        onClick = {
          if (currentStep < 5) {
            currentStep++
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.RIGHT_TURN)
          } else {
            HapticAudioFeedbackUtil.triggerFeedback(context, HapticPatternType.STOP_HAZARD)
            onBack()
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(58.dp)
          .testTag("booking_next_button"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Primary,
          contentColor = OnPrimary
        )
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = if (currentStep < 5) (if (isHindi) "आगे बढ़ें (NEXT)" else "NEXT") else (if (isHindi) "पूर्ण एवं मुख्य पृष्ठ पर जाएं" else "DONE & RETURN HOME"),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
          )
        }
      }

      TextButton(
        onClick = { showHelpDialog = !showHelpDialog },
        modifier = Modifier.fillMaxWidth()
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.HelpOutline,
          contentDescription = null,
          tint = TextMuted,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isHindi) "सहायता या वॉयस रीडआउट की आवश्यकता है?" else "Need assistance or voice readout?",
          color = TextMuted,
          fontSize = 13.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}
