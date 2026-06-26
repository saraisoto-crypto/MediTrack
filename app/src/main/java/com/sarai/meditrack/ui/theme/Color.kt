package com.sarai.meditrack.ui.theme

import androidx.compose.ui.graphics.Color

// ══════════════════════════════════════════════════════════════════════════════
// MediTrack — Paleta Morado Vibrante / Lavanda / Fucsia
// ══════════════════════════════════════════════════════════════════════════════

// Morados principales
val PurpleDeep      = Color(0xFF3A0CA3)
val PurpleMid       = Color(0xFF560BAD)
val PurpleVibrant   = Color(0xFF7B2FBE)
val PurpleLight     = Color(0xFFB185DB)
val PurpleSurface   = Color(0xFFF0E6FF)
val PurpleSoftBg    = Color(0xFFF8F3FF)

// Acento fucsia/magenta
val FuchsiaBright   = Color(0xFFE040FB)
val FuchsiaSoft     = Color(0xFFFCE4FF)
val FuchsiaDark     = Color(0xFFAA00FF)

// Lavanda
val LavenderMid     = Color(0xFF9B72CF)
val LavenderLight   = Color(0xFFD7B8F3)
val LavenderSurface = Color(0xFFEDE0FF)

// Neutrales cálidos
val OffWhite        = Color(0xFFFDFAFF)
val SurfaceCard     = Color(0xFFF5F0FF)
val OutlineLight    = Color(0xFFDDD5EC)
val OutlineMid      = Color(0xFFBBB0D4)

// Texto
val TextPrimary     = Color(0xFF1A1028)
val TextSecondary   = Color(0xFF4A3F5C)
val TextTertiary    = Color(0xFF8C7FAA)

// Semántico
val SuccessGreen    = Color(0xFF2E7D32)
val SuccessSurface  = Color(0xFFE8F5E9)
val ErrorRed        = Color(0xFFB71C1C)
val ErrorSurface    = Color(0xFFFFEBEE)

// ── Alias usados por Theme.kt y pantallas (compatibilidad) ───────────────────
// Theme.kt (esquema oscuro)
val PurpleElectric     = PurpleVibrant
val PinkAccent         = FuchsiaBright
val CyanAccent         = Color(0xFF26C6DA)
val BackgroundDark     = Color(0xFF120B1E)
val SurfaceDark        = Color(0xFF1C1228)
val SurfaceVariantDark = Color(0xFF2E2140)

// Pantallas (Detail/List/Quote) — paleta "teal/coral" mapeada a tu morado actual
val TealMid      = PurpleMid
val TealDeep     = PurpleDeep
val CoralBright  = FuchsiaBright
val TealSurface  = PurpleSurface

// ── Light Scheme ─────────────────────────────────────────────────────────────
val md_theme_light_primary              = PurpleMid
val md_theme_light_onPrimary            = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer     = PurpleSurface
val md_theme_light_onPrimaryContainer   = PurpleDeep

val md_theme_light_secondary            = LavenderMid
val md_theme_light_onSecondary          = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer   = LavenderSurface
val md_theme_light_onSecondaryContainer = Color(0xFF21005D)

val md_theme_light_tertiary             = FuchsiaBright
val md_theme_light_onTertiary           = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer    = FuchsiaSoft
val md_theme_light_onTertiaryContainer  = Color(0xFF4A0072)

val md_theme_light_error                = ErrorRed
val md_theme_light_onError              = Color(0xFFFFFFFF)
val md_theme_light_errorContainer       = ErrorSurface
val md_theme_light_onErrorContainer     = Color(0xFF410002)

val md_theme_light_background           = OffWhite
val md_theme_light_onBackground         = TextPrimary
val md_theme_light_surface              = Color(0xFFFFFFFF)
val md_theme_light_onSurface            = TextPrimary
val md_theme_light_surfaceVariant       = SurfaceCard
val md_theme_light_onSurfaceVariant     = TextSecondary
val md_theme_light_outline              = OutlineLight
val md_theme_light_outlineVariant       = OutlineMid
val md_theme_light_inverseSurface       = Color(0xFF2B2035)
val md_theme_light_inverseOnSurface     = Color(0xFFF3EEFF)
val md_theme_light_inversePrimary       = LavenderLight

// ── Dark Scheme ──────────────────────────────────────────────────────────────
val md_theme_dark_primary               = LavenderLight
val md_theme_dark_onPrimary             = PurpleDeep
val md_theme_dark_primaryContainer      = Color(0xFF3D1A7A)
val md_theme_dark_onPrimaryContainer    = Color(0xFFE9DAFF)

val md_theme_dark_secondary             = Color(0xFFCDA8F0)
val md_theme_dark_onSecondary           = Color(0xFF341A5C)
val md_theme_dark_secondaryContainer    = Color(0xFF4C2A75)
val md_theme_dark_onSecondaryContainer  = Color(0xFFE9DAFF)

val md_theme_dark_tertiary              = Color(0xFFF5ABFF)
val md_theme_dark_onTertiary            = Color(0xFF57006B)
val md_theme_dark_tertiaryContainer     = Color(0xFF7C0099)
val md_theme_dark_onTertiaryContainer   = Color(0xFFFFD6FF)

val md_theme_dark_error                 = Color(0xFFFFB4AB)
val md_theme_dark_onError               = Color(0xFF690005)
val md_theme_dark_errorContainer        = Color(0xFF93000A)
val md_theme_dark_onErrorContainer      = Color(0xFFFFDAD6)

val md_theme_dark_background            = Color(0xFF120B1E)
val md_theme_dark_onBackground          = Color(0xFFEDE5FF)
val md_theme_dark_surface               = Color(0xFF1C1228)
val md_theme_dark_onSurface             = Color(0xFFEDE5FF)
val md_theme_dark_surfaceVariant        = Color(0xFF2E2140)
val md_theme_dark_onSurfaceVariant      = Color(0xFFCDBFE8)
val md_theme_dark_outline               = Color(0xFF4A3D60)
val md_theme_dark_outlineVariant        = Color(0xFF352748)
val md_theme_dark_inverseSurface        = Color(0xFFEDE5FF)
val md_theme_dark_inverseOnSurface      = Color(0xFF1C1228)
val md_theme_dark_inversePrimary        = PurpleMid