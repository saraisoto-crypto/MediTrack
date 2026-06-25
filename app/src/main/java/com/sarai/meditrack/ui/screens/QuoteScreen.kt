package com.sarai.meditrack.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarai.meditrack.MediTrackApp
import com.sarai.meditrack.ui.theme.*
import com.sarai.meditrack.viewmodel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(navController: NavController) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTrackApp
    val viewModel: QuoteViewModel = viewModel(
        factory = QuoteViewModel.factory(app.repository)
    )
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(TealDeep, Color(0xFF102A28)),
                    start = Offset(0f, 0f),
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        // Decoración: círculos difusos
        Box(
            modifier = Modifier
                .size(350.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-80).dp)
                .alpha(0.06f)
                .background(Color.White, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 60.dp)
                .alpha(0.05f)
                .background(CoralBright, CircleShape)
        )

        // Top bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        "INSPIRACIÓN",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.6f),
                        letterSpacing = 2.5.sp
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .background(Color.White.copy(alpha = 0.10f), CircleShape)
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        // Contenido centrado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Crossfade(
                targetState = uiState.isLoading,
                label = "QuoteFade",
                animationSpec = tween(600)
            ) { isLoading ->
                when {
                    isLoading        -> QuoteLoadingView()
                    uiState.error != null -> QuoteErrorView(
                        message = uiState.error!!,
                        onRetry = { viewModel.loadQuote() }
                    )
                    else             -> QuoteContentView(
                        quote  = uiState.quote,
                        author = uiState.author,
                        onRefresh = { viewModel.loadQuote() }
                    )
                }
            }
        }
    }
}

// ── Contenido principal de la frase ──────────────────────────────────────────

@Composable
private fun QuoteContentView(quote: String, author: String, onRefresh: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(36.dp)
    ) {
        // Card editorial
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.07f)
            ),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(modifier = Modifier.padding(32.dp)) {
                // Comillas decorativas grandes
                Text(
                    text = "\u201C",
                    fontSize = 120.sp,
                    color = CoralBright.copy(alpha = 0.25f),
                    fontWeight = FontWeight.Black,
                    lineHeight = 80.sp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-8).dp, y = (-16).dp)
                )

                Column(
                    modifier = Modifier.padding(top = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    // Texto de la frase
                    AnimatedContent(
                        targetState = quote,
                        label = "QuoteText",
                        transitionSpec = {
                            fadeIn(tween(400)) togetherWith fadeOut(tween(200))
                        }
                    ) { q ->
                        Text(
                            text = q,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontStyle  = FontStyle.Italic,
                                lineHeight = 34.sp,
                                fontWeight = FontWeight.Light,
                                textAlign  = TextAlign.Center
                            ),
                            color = Color.White
                        )
                    }

                    // Separador + autor
                    AnimatedContent(
                        targetState = author,
                        label = "AuthorText",
                        transitionSpec = {
                            fadeIn(tween(400)) togetherWith fadeOut(tween(200))
                        }
                    ) { a ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Línea coral
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(2.dp)
                                    .background(CoralBright, RoundedCornerShape(1.dp))
                            )
                            Text(
                                text = a.ifEmpty { "Anónimo" }.uppercase(),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    letterSpacing = 2.sp,
                                    fontWeight    = FontWeight.Bold
                                ),
                                color = CoralBright
                            )
                        }
                    }
                }
            }
        }

        // Botón nueva frase
        Button(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth(0.80f)
                .height(56.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CoralBright),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(Icons.Rounded.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
            Spacer(Modifier.width(10.dp))
            Text(
                "Nueva frase",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }

        // Hint
        Text(
            "Cada frase es un recordatorio de que tu bienestar importa.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.35f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

// ── Loading ───────────────────────────────────────────────────────────────────

@Composable
private fun QuoteLoadingView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(52.dp),
            color = CoralBright,
            strokeWidth = 4.dp,
            trackColor = Color.White.copy(alpha = 0.10f)
        )
        Text(
            "Buscando inspiración...",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.55f)
        )
    }
}

// ── Error ─────────────────────────────────────────────────────────────────────

@Composable
private fun QuoteErrorView(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = CoralBright
            )
        }
        Text(
            "Sin conexión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor   = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor   = Color.White.copy(alpha = 0.3f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.3f))
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Reintentar", fontWeight = FontWeight.SemiBold)
        }
    }
}