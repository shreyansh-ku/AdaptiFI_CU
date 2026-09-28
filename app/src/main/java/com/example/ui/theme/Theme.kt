package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.model.AppUserSettings

val LocalUserSettings = staticCompositionLocalOf { AppUserSettings() }

private val AdaptiFiLightColorScheme = lightColorScheme(
  primary = Primary,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = Secondary,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = Tertiary,
  onTertiary = OnTertiary,
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer,
  background = Surface,
  onBackground = OnSurface,
  surface = Surface,
  onSurface = OnSurface,
  surfaceVariant = SurfaceVariant,
  onSurfaceVariant = OnSurfaceVariant,
  inverseSurface = InverseSurface,
  inverseOnSurface = InverseOnSurface,
  outline = Outline,
  outlineVariant = OutlineVariant,
  error = Error,
  onError = OnError,
  errorContainer = ErrorContainer,
  onErrorContainer = OnErrorContainer
)

private val AdaptiFiHighContrastColorScheme = lightColorScheme(
  primary = HighContrastPrimary,
  onPrimary = Color(0xFF000000),
  primaryContainer = Color(0xFF1E3A8A),
  onPrimaryContainer = Color(0xFFFFFFFF),
  secondary = HighContrastSecondary,
  onSecondary = Color(0xFF000000),
  secondaryContainer = Color(0xFF064E3B),
  onSecondaryContainer = Color(0xFFFFFFFF),
  tertiary = HighContrastTertiary,
  onTertiary = Color(0xFF000000),
  tertiaryContainer = Color(0xFF78350F),
  onTertiaryContainer = Color(0xFFFFFFFF),
  background = Color(0xFF000000),
  onBackground = Color(0xFFFFFFFF),
  surface = Color(0xFF121212),
  onSurface = Color(0xFFFFFFFF),
  surfaceVariant = Color(0xFF1E293B),
  onSurfaceVariant = Color(0xFFF1F5F9),
  inverseSurface = Color(0xFFFFFFFF),
  inverseOnSurface = Color(0xFF000000),
  outline = Color(0xFFFFFFFF),
  outlineVariant = Color(0xFFE2E8F0),
  error = Color(0xFFFF5252),
  onError = Color(0xFF000000),
  errorContainer = Color(0xFF7F1D1D),
  onErrorContainer = Color(0xFFFFFFFF)
)

private fun scaledTypography(scale: Float, base: Typography): Typography {
  fun scaleStyle(style: TextStyle): TextStyle =
    style.copy(
      fontSize = (style.fontSize.value * scale).sp,
      lineHeight = if (style.lineHeight.value > 0) (style.lineHeight.value * scale).sp else style.lineHeight
    )

  return Typography(
    displayLarge = scaleStyle(base.displayLarge),
    displayMedium = scaleStyle(base.displayMedium),
    displaySmall = scaleStyle(base.displaySmall),
    headlineLarge = scaleStyle(base.headlineLarge),
    headlineMedium = scaleStyle(base.headlineMedium),
    headlineSmall = scaleStyle(base.headlineSmall),
    titleLarge = scaleStyle(base.titleLarge),
    titleMedium = scaleStyle(base.titleMedium),
    titleSmall = scaleStyle(base.titleSmall),
    bodyLarge = scaleStyle(base.bodyLarge),
    bodyMedium = scaleStyle(base.bodyMedium),
    bodySmall = scaleStyle(base.bodySmall),
    labelLarge = scaleStyle(base.labelLarge),
    labelMedium = scaleStyle(base.labelMedium),
    labelSmall = scaleStyle(base.labelSmall)
  )
}

@Composable
fun AdaptiFiTheme(
  userSettings: AppUserSettings = AppUserSettings(),
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (userSettings.highContrast) {
    AdaptiFiHighContrastColorScheme
  } else {
    AdaptiFiLightColorScheme
  }

  val activeTypography = scaledTypography(userSettings.fontScale, Typography)

  CompositionLocalProvider(LocalUserSettings provides userSettings) {
    MaterialTheme(
      colorScheme = colorScheme,
      typography = activeTypography,
      content = content
    )
  }
}
