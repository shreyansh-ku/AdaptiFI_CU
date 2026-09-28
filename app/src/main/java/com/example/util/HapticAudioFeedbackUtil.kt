package com.example.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.example.model.HapticPatternType

object HapticAudioFeedbackUtil {

  @Suppress("DEPRECATION")
  fun triggerFeedback(context: Context, pattern: HapticPatternType) {
    // 1. Trigger Vibration
    try {
      val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
      } else {
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
      }

      vibrator?.let { vib ->
        if (vib.hasVibrator()) {
          when (pattern) {
            HapticPatternType.LEFT_TURN -> {
              // 2 short buzzes: [delay, buzz, pause, buzz]
              val timings = longArrayOf(0, 100, 80, 100)
              val amplitudes = intArrayOf(0, 180, 0, 220)
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
              } else {
                vib.vibrate(timings, -1)
              }
            }
            HapticPatternType.RIGHT_TURN -> {
              // 2 short crisp buzzes with different cadence
              val timings = longArrayOf(0, 80, 60, 120)
              val amplitudes = intArrayOf(0, 200, 0, 240)
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
              } else {
                vib.vibrate(timings, -1)
              }
            }
            HapticPatternType.STOP_HAZARD -> {
              // 1 continuous long urgent pulse (500ms)
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
              } else {
                vib.vibrate(500)
              }
            }
          }
        }
      }
    } catch (_: Exception) {
      // Gracefully ignore if running in environment without physical hardware motor
    }

    // 2. Play Audio Tone chime (System ToneGenerator)
    try {
      val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 75)
      when (pattern) {
        HapticPatternType.LEFT_TURN -> {
          toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
        }
        HapticPatternType.RIGHT_TURN -> {
          toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 120)
        }
        HapticPatternType.STOP_HAZARD -> {
          toneGen.startTone(ToneGenerator.TONE_PROP_PROMPT, 300)
        }
      }
    } catch (_: Exception) {
      // Gracefully handle tone generator creation failure
    }
  }

  fun triggerMicChime(context: Context) {
    try {
      val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60)
      toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 80)
    } catch (_: Exception) {}
  }
}
