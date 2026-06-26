package com.sarai.meditrack.ui.theme

import androidx.compose.ui.graphics.Color

// ── Paleta morada dark llamativa ──────────────────────────────────────────────

// Usadas en MaterialTheme (Theme.kt)
val PurpleNeon          = Color(0xFFB388FF)
val PurpleVibrant       = Color(0xFF8B5CF6)
val PurpleElectric      = Color(0xFFA855F7)
val PurpleDeep          = Color(0xFF6D28D9)
val PurplePrimaryDark   = Color(0xFF4A148C)

// Alias usados en screens y componentes
val PurpleLight         = Color(0xFFD4AAFF)   // íconos claros, tints
val PurpleMid           = Color(0xFF9B59F5)   // primario en campos, chips
val PurpleSurface       = Color(0xFF2A1D4D)   // fondos de cards suaves
val PurpleSoftBg        = Color(0xFF1E1433)   // fondo alternativo

// Fondos y superficies
val BackgroundDark      = Color(0xFF120B1F)
val SurfaceDark         = Color(0xFF1E1433)
val SurfaceVariantDark  = Color(0xFF2A1D4D)

// Acentos
val PinkAccent          = Color(0xFFFF4FD8)
val FuchsiaBright       = Color(0xFFFF4FD8)   // alias de PinkAccent para FAB
val CyanAccent          = Color(0xFF00E5FF)

// Texto
val TextPrimary         = Color(0xFFF5F0FF)
val TextSecondary       = Color(0xFFB8A9D9)

// Semánticos
val ErrorRed            = Color(0xFFFF5370)
val ErrorSurface        = Color(0xFF3D1A1A)
val SuccessGreen        = Color(0xFF4ADE80)
val SuccessSurface      = Color(0xFF1A3D2A)

// Bordes y outlines
val OutlineLight        = Color(0xFF4A3575)
val OutlineMid          = Color(0xFF6A4FAA)

// Neutrales (compatibilidad con screens anteriores)
val TealDeep            = Color(0xFF1A0B2E)
val TealMid             = Color(0xFFA855F7)
val TealSurface         = Color(0xFF2A1D4D)
val CoralBright         = Color(0xFFFF4FD8)
val CoralSoft           = Color(0xFF3D2A5C)
val OffWhite            = Color(0xFF1E1433)

// ── Light Scheme (MaterialTheme) ──────────────────────────────────────────────
val md_theme_light_primary              = PurpleElectric
val md_theme_light_onPrimary            = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer     = PurpleSurface
val md_theme_light_onPrimaryContainer   = PurpleLight
val md_theme_light_secondary            = PurpleNeon
val md_theme_light_onSecondary          = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer   = SurfaceVariantDark
val md_theme_light_onSecondaryContainer = PurpleLight
val md_theme_light_tertiary             = PinkAccent
val md_theme_light_onTertiary           = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer    = CoralSoft
val md_theme_light_onTertiaryContainer  = Color(0xFFFFD6FF)
val md_theme_light_error                = ErrorRed
val md_theme_light_onError              = Color(0xFFFFFFFF)
val md_theme_light_errorContainer       = ErrorSurface
val md_theme_light_onErrorContainer     = Color(0xFFFFDAD6)
val md_theme_light_background           = BackgroundDark
val md_theme_light_onBackground         = TextPrimary
val md_theme_light_surface              = SurfaceDark
val md_theme_light_onSurface            = TextPrimary
val md_theme_light_surfaceVariant       = SurfaceVariantDark
val md_theme_light_onSurfaceVariant     = TextSecondary
val md_theme_light_outline              = OutlineLight
val md_theme_light_outlineVariant       = OutlineMid
val md_theme_light_inverseSurface       = TextPrimary
val md_theme_light_inverseOnSurface     = BackgroundDark
val md_theme_light_inversePrimary       = PurpleDeep

// ── Dark Scheme (mismo que light para mantener dark look) ─────────────────────
val md_theme_dark_primary               = PurpleNeon
val md_theme_dark_onPrimary             = PurpleDeep
val md_theme_dark_primaryContainer      = PurplePrimaryDark
val md_theme_dark_onPrimaryContainer    = PurpleLight
val md_theme_dark_secondary             = PurpleLight
val md_theme_dark_onSecondary           = PurplePrimaryDark
val md_theme_dark_secondaryContainer    = SurfaceVariantDark
val md_theme_dark_onSecondaryContainer  = PurpleLight
val md_theme_dark_tertiary              = PinkAccent
val md_theme_dark_onTertiary            = Color(0xFF57006B)
val md_theme_dark_tertiaryContainer     = CoralSoft
val md_theme_dark_onTertiaryContainer   = Color(0xFFFFD6FF)
val md_theme_dark_error                 = ErrorRed
val md_theme_dark_onError               = Color(0xFF690005)
val md_theme_dark_errorContainer        = ErrorSurface
val md_theme_dark_onErrorContainer      = Color(0xFFFFDAD6)
val md_theme_dark_background            = BackgroundDark
val md_theme_dark_onBackground          = TextPrimary
val md_theme_dark_surface               = SurfaceDark
val md_theme_dark_onSurface             = TextPrimary
val md_theme_dark_surfaceVariant        = SurfaceVariantDark
val md_theme_dark_onSurfaceVariant      = TextSecondary
val md_theme_dark_outline               = OutlineLight
val md_theme_dark_outlineVariant        = OutlineMid
val md_theme_dark_inverseSurface        = TextPrimary
val md_theme_dark_inverseOnSurface      = BackgroundDark
val md_theme_dark_inversePrimary        = PurpleElectric
val TextLight = Color(0xFFF5F0FF) // alias de TextPrimary
val Error     = Color(0xFFFF5370)   // alias de ErrorRed
val TextMedium = Color(0xFFB8A9D9)   // alias de TextSecondary