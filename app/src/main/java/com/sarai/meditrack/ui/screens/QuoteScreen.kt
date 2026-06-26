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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
            .background(BackgroundDark)
    ) {
        QuoteBackgroundDecoration()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Dosis de Inspiración",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = uiState.isLoading,
                    label = "QuoteFade",
                    animationSpec = tween(600)
                ) { isLoading ->
                    when {
                        isLoading -> QuoteLoadingView()
                        uiState.error != null -> QuoteErrorView(
                            message = uiState.error!!,
                            onRetry = { viewModel.loadQuote() }
                        )
                        else -> QuoteContentView(
                            quote = uiState.quote,
                            author = uiState.author,
                            onRefresh = { viewModel.loadQuote() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteBackgroundDecoration() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .blur(80.dp)
                .alpha(0.35f)
                .background(PurpleElectric, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-80).dp, y = 100.dp)
                .blur(80.dp)
                .alpha(0.3f)
                .background(PinkAccent, CircleShape)
        )
    }
}

@Composable
private fun QuoteContentView(quote: String, author: String, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.elevatedCardColors(containerColor = SurfaceDark),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.FormatQuote,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).alpha(0.3f),
                    tint = PurpleLight
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = quote,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 38.sp,
                        textAlign = TextAlign.Center
                    ),
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(Brush.linearGradient(listOf(PurpleElectric, PinkAccent)), CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = author.ifEmpty { "Anónimo" }.uppercase(),
                    style = MaterialTheme.typography.labelLarge.copy(
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Black
                    ),
                    color = PurpleLight
                )
            }
        }

        Button(
            onClick = onRefresh,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.linearGradient(listOf(PurpleElectric, PinkAccent)), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Refresh, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("NUEVA FRASE", fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun QuoteLoadingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator(color = PurpleElectric)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Buscando inspiración...", style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

@Composable
private fun QuoteErrorView(message: String, onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
    ) {
        Icon(Icons.Rounded.ErrorOutline, null, modifier = Modifier.size(64.dp), tint = Error)
        Spacer(modifier = Modifier.height(16.dp))
        Text(message, textAlign = TextAlign.Center, color = Color.White)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = PurpleElectric)
        ) {
            Text("Reintentar", color = Color.White)
        }
    }
}