package com.escolaapp.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppColors {
    val Primary = Color(0xFF0040A1)
    val OnPrimary = Color(0xFFFFFFFF)

    val PrimaryVariant = Color(0xFF185FA5)
    val OnPrimaryVariant = Color(0xFFFFFFFF)

    val Secondary = Color(0xFF8B5000)
    val OnSecondary = Color(0xFFFFFFFF)

    val SecondaryVariant = Color(0xFFE65100)
    val OnSecondaryVariant = Color(0xFFFFFFFF)

    val Background = Color(0xFFFAF8FF)
    val OnBackground = Color(0xFF1C1B1F)

    val Surface = Color(0xFFFFFFFF)
    val OnSurface = Color(0xFF1C1B1F)
    val SurfaceVariant = Color(0xFFE7E0EC)
    val OnSurfaceVariant = Color(0xFF424654)

    val SurfaceContainer = Color(0xFFEDEDF8)
    val SurfaceContainerLow = Color(0xFFF2F3FE)
    val SurfaceContainerHigh = Color(0xFFCAC4D0)

    val Outline = Color(0xFF79747E)
    val OutlineVariant = Color(0xFFC3C6D6)

    val Error = Color(0xFFBA1A1A)
    val OnError = Color(0xFFFFFFFF)
    val ErrorContainer = Color(0xFFFFDAD6)

    val Success = Color(0xFF1565C0)
    val SuccessContainer = Color(0xFFE8F4FD)

    val Warning = Color(0xFFE65100)
    val WarningContainer = Color(0xFFFFF3E0)

    val SurfaceContainerLowest = Color(0xFFFFFFFF)

    val CardBackground = Color(0xFFFFFFFF)
    val CardBorder = Color(0xFFE7E0EC)

    val AccentOrange = Color(0xFFE67E22)
    val AccentOrangeContainer = Color(0xFFFFF3E0)

    val AccentGreen = Color(0xFF4CAF50)
    val AccentGreenContainer = Color(0xFFEAF3DE)

    val AccentPurple = Color(0xFF7C4DFF)
    val AccentPurpleContainer = Color(0xFFEEEDFE)

    val AccentTeal = Color(0xFF009688)
    val AccentTealContainer = Color(0xFFD0F8F0)

    val AccentPink = Color(0xFFE91E63)
    val AccentPinkContainer = Color(0xFFFCE4EC)

    val AccentIndigo = Color(0xFF3F51B5)
    val AccentIndigoContainer = Color(0xFFAFA9EC)
}

private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    primaryContainer = AppColors.SuccessContainer,
    onPrimaryContainer = AppColors.Primary,

    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    secondaryContainer = AppColors.WarningContainer,
    onSecondaryContainer = AppColors.Secondary,

    tertiary = AppColors.AccentPurple,
    onTertiary = AppColors.OnPrimary,
    tertiaryContainer = AppColors.AccentPurpleContainer,
    onTertiaryContainer = AppColors.AccentPurple,

    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.ErrorContainer,
    onErrorContainer = AppColors.Error,

    background = AppColors.Background,
    onBackground = AppColors.OnBackground,

    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.OnSurfaceVariant,

    outline = AppColors.Outline,
    outlineVariant = AppColors.OutlineVariant,

    inverseSurface = AppColors.OnSurface,
    inverseOnSurface = AppColors.Surface,
    inversePrimary = AppColors.Primary,
)

private val DarkColorScheme = darkColorScheme(
    primary = AppColors.PrimaryVariant,
    onPrimary = AppColors.OnPrimaryVariant,
    primaryContainer = AppColors.Primary,
    onPrimaryContainer = AppColors.OnPrimary,

    secondary = AppColors.Warning,
    onSecondary = AppColors.OnSecondaryVariant,
    secondaryContainer = AppColors.Secondary,
    onSecondaryContainer = AppColors.OnSecondary,

    tertiary = AppColors.AccentPurple,
    onTertiary = AppColors.OnPrimary,
    tertiaryContainer = AppColors.AccentPurple,
    onTertiaryContainer = AppColors.OnPrimary,

    error = AppColors.Error,
    onError = AppColors.OnError,
    errorContainer = AppColors.Error,
    onErrorContainer = AppColors.OnError,

    background = AppColors.OnBackground,
    onBackground = AppColors.Surface,

    surface = AppColors.OnSurface,
    onSurface = AppColors.Surface,
    surfaceVariant = AppColors.SurfaceContainerHigh,
    onSurfaceVariant = AppColors.SurfaceContainerLow,

    outline = AppColors.OutlineVariant,
    outlineVariant = AppColors.Outline,

    inverseSurface = AppColors.Surface,
    inverseOnSurface = AppColors.OnSurface,
    inversePrimary = AppColors.Primary,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}