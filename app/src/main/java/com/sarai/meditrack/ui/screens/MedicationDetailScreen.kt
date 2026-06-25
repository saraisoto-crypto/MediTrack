package com.sarai.meditrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarai.meditrack.MediTrackApp
import com.sarai.meditrack.ui.theme.*
import com.sarai.meditrack.viewmodel.MedicationFormViewModel

private val categoryColorMap = mapOf(
    "Analgésico"            to Color(0xFFE57373),
    "Antibiótico"           to Color(0xFF64B5F6),
    "Vitamina / Suplemento" to Color(0xFF81C784),
    "Antiinflamatorio"      to Color(0xFFFFB74D),
    "Antiácido"             to Color(0xFF9575CD),
    "Antihistamínico"       to Color(0xFF4DB6AC),
    "Otro"                  to Color(0xFF90A4AE),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDetailScreen(navController: NavController, medicationId: Int) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTrackApp
    val viewModel: MedicationFormViewModel = viewModel(
        factory = MedicationFormViewModel.factory(app.repository)
    )
    val medication by viewModel.medication.collectAsState()

    LaunchedEffect(medicationId) { viewModel.loadMedication(medicationId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .background(Color.White.copy(alpha = 0.20f), CircleShape)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    FilledTonalIconButton(
                        onClick = { navController.navigate("medication_form?id=$medicationId") },
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(Color.White.copy(alpha = 0.20f), CircleShape),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor   = Color.White
                        )
                    ) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Editar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                expandedHeight = 56.dp
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        medication?.let { med ->
            val catColor = categoryColorMap[med.category] ?: TealMid

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                // ── Hero banner con gradiente ──────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                ) {
                    // Fondo gradiente usando el color de categoría
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(TealDeep, catColor.copy(alpha = 0.7f)),
                                    start = Offset(0f, 0f),
                                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                                )
                            )
                    )

                    // Círculos decorativos
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 60.dp, y = (-40).dp)
                            .background(Color.White.copy(alpha = 0.05f), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.BottomStart)
                            .offset(x = (-30).dp, y = 30.dp)
                            .background(Color.White.copy(alpha = 0.07f), CircleShape)
                    )

                    // Contenido del hero
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)           // respeta status bar
                            .padding(horizontal = 28.dp, vertical = 20.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Icono grande
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.Medication,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // Nombre
                        Text(
                            text = med.name,
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Spacer(Modifier.height(10.dp))

                        // Badge de categoría
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.20f)
                        ) {
                            Text(
                                text = med.category,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Curva superior del contenido blanco
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        // ── Grid de info rápida ────────────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailInfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Rounded.Scale,
                                label = "Dosis",
                                value = med.dose,
                                accentColor = CoralBright
                            )
                            DetailInfoCard(
                                modifier = Modifier.weight(1f),
                                icon = Icons.Rounded.AccessTime,
                                label = "Hora",
                                value = med.time,
                                accentColor = TealMid
                            )
                        }

                        DetailInfoCard(
                            modifier = Modifier.fillMaxWidth(),
                            icon = Icons.Rounded.Repeat,
                            label = "Frecuencia",
                            value = med.frequency,
                            accentColor = Color(0xFF3F6AC4)
                        )

                        // ── Notas ──────────────────────────────────────────
                        if (med.notes.isNotEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                ),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Rounded.StickyNote2,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Notas",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Spacer(Modifier.height(12.dp))
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        thickness = 1.dp
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        text = med.notes,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 26.sp
                                    )
                                }
                            }
                        }

                        // ── Botón editar en detalle ────────────────────────
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate("medication_form?id=$medicationId") },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealMid)
                        ) {
                            Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Editar medicamento", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

        } ?: Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = TealMid)
        }
    }
}

// ── DetailInfoCard ────────────────────────────────────────────────────────────

@Composable
fun DetailInfoCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(accentColor.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value.ifEmpty { "—" },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}