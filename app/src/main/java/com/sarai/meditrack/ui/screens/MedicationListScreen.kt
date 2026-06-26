package com.sarai.meditrack.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarai.meditrack.MediTrackApp
import com.sarai.meditrack.data.local.Medication
import com.sarai.meditrack.ui.theme.*
import com.sarai.meditrack.viewmodel.MedicationListViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.sarai.meditrack.notifications.NotificationHelper


// -- Colores por categoría ----------------------------------------------------
private val categoryColors = mapOf(
    "Analgésico"            to Color(0xFFE57373),
    "Antibiótico"           to Color(0xFF64B5F6),
    "Vitamina / Suplemento" to Color(0xFF81C784),
    "Antiinflamatorio"      to Color(0xFFFFB74D),
    "Antiácido"             to Color(0xFF9575CD),
    "Antihistamínico"       to Color(0xFF4DB6AC),
    "Otro"                  to Color(0xFF90A4AE),
)

private fun categoryColor(cat: String) =
    categoryColors[cat] ?: Color(0xFF90A4AE)

// -- Helpers de saludo y próxima toma -----------------------------------------

private fun saludoSegunHora(): String {
    val hour = LocalTime.now().hour
    return when {
        hour < 12 -> "Buenos días"
        hour < 19 -> "Buenas tardes"
        else      -> "Buenas noches"
    }
}

/**
 * Busca la próxima toma comparando el campo `time` ("HH:mm") de cada
 * medicamento contra la hora actual. Si todas las horas de hoy ya pasaron,
 * devuelve la más temprana (asumiendo que se repite al día siguiente).
 */
private fun proximaToma(medications: List<Medication>): Medication? {
    if (medications.isEmpty()) return null
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val now = LocalTime.now()

    val parsed = medications.mapNotNull { med ->
        runCatching { LocalTime.parse(med.time.trim(), formatter) }.getOrNull()?.let { med to it }
    }
    if (parsed.isEmpty()) return medications.firstOrNull()

    val proxima = parsed.filter { it.second.isAfter(now) }.minByOrNull { it.second }
    return proxima?.first ?: parsed.minByOrNull { it.second }?.first
}

private fun minutosHasta(horaTexto: String): Long? {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val hora = runCatching { LocalTime.parse(horaTexto.trim(), formatter) }.getOrNull() ?: return null
    val now = LocalTime.now()
    val diff = java.time.Duration.between(now, hora).toMinutes()
    return if (diff >= 0) diff else diff + 24 * 60
}

// ----------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationListScreen(navController: NavController, onLogout: () -> Unit = {}) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTrackApp
    val viewModel: MedicationListViewModel = viewModel(
        factory = MedicationListViewModel.factory(app.repository)
    )
    val medications by viewModel.medications.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            // -- Hero Header con saludo dinámico ----------------------------
            item {
                HeroHeader(onQuoteTap = { navController.navigate("quotes") }, onLogout = onLogout)
            }

            // -- Próxima toma (real, calculada de tus datos) ----------------
            item {
                NextDoseCard(
                    medication = proximaToma(medications),
                    onClick = { med -> navController.navigate("medication_detail/${med.id}") }
                )
            }

            // -- Stats reales ---------------------------------------------
            item {
                StatsRow(medications = medications)
            }

            // -- Sección de lista ---------------------------------------------
            if (medications.isEmpty()) {
                item { EmptyState() }
            } else {
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mis medicamentos",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(Modifier.weight(1f))
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${medications.size}",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
                items(medications) { med ->
                    MedicationCard(
                        medication = med,
                        onDelete = { viewModel.deleteMedication(med) },
                        onClick = { navController.navigate("medication_detail/${med.id}") }
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
        // Botón temporal para probar notificación local
        Button(
            onClick = {
                NotificationHelper.sendMedicationReminder(
                    context = context,
                    medicationName = "Paracetamol",
                    dose = "500mg",
                    notificationId = 1
                )
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PurpleMid)
        ) {
            Text("🔔 Probar")
        }

        // -- FAB --------------------------------------------------------------
        FloatingActionButton(
            onClick = { navController.navigate("medication_form?id=-1") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(64.dp)
                .shadow(12.dp, CircleShape),
            shape = CircleShape,
            containerColor = FuchsiaBright,
            contentColor = Color.White
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Añadir", modifier = Modifier.size(28.dp))
        }
    }
}

// -- Hero Header --------------------------------------------------------------

@Composable
private fun HeroHeader(onQuoteTap: () -> Unit, onLogout: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF6D28D9),
                            Color(0xFFA855F7),
                            Color(0xFFFF4FD8)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-70).dp)
                .background(Color.White.copy(alpha = 0.10f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.HealthAndSafety,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    "MediTrack",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    modifier = Modifier.clickable { onQuoteTap() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Rounded.AutoAwesome, contentDescription = null, tint = Color(0xFF6D28D9), modifier = Modifier.size(16.dp))
                        Text("Frase del día", style = MaterialTheme.typography.labelMedium, color = Color(0xFF6D28D9), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(8.dp))
                // Botón cerrar sesión
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White.copy(alpha = 0.18f), CircleShape)
                ) {
                    Icon(
                        Icons.Rounded.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column {
                Text(
                    "${saludoSegunHora()} 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Tu salud, bajo control.",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                    color = Color.White
                )
            }
        }
    }
}

// -- Próxima toma -------------------------------------------------------------

@Composable
private fun NextDoseCard(medication: Medication?, onClick: (Medication) -> Unit) {
    if (medication == null) return

    val minutos = minutosHasta(medication.time)
    val subtitulo = when {
        minutos == null -> "Hora no definida"
        minutos <= 0L    -> "Es ahora"
        minutos < 60L    -> "En $minutos min"
        else             -> "En ${minutos / 60}h ${minutos % 60}min"
    }
    val catColor = categoryColor(medication.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable { onClick(medication) }
            .shadow(10.dp, RoundedCornerShape(22.dp), spotColor = catColor),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(catColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Medication, contentDescription = null, tint = catColor, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "PRÓXIMA TOMA",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    medication.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${medication.time} · $subtitulo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// -- Stats reales -------------------------------------------------------------

@Composable
private fun StatsRow(medications: List<Medication>) {
    val totalCategorias = medications.map { it.category }.distinct().size

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Medication,
            value = "${medications.size}",
            label = if (medications.size == 1) "Medicamento" else "Medicamentos",
            color = Color(0xFFA855F7)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.Category,
            value = "$totalCategorias",
            label = if (totalCategorias == 1) "Categoría" else "Categorías",
            color = Color(0xFFFF4FD8)
        )
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }
        }
    }
}

// -- Medication Card ----------------------------------------------------------

@Composable
fun MedicationCard(
    medication: Medication,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Rounded.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar medicamento?", fontWeight = FontWeight.Bold) },
            text = { Text("Se eliminará \"${medication.name}\" de forma permanente.") },
            confirmButton = {
                Button(
                    onClick = { onDelete(); showDeleteDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    val catColor = categoryColor(medication.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(catColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Medication,
                    contentDescription = null,
                    tint = catColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Rounded.Schedule, contentDescription = null, modifier = Modifier.size(13.dp), tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = medication.time,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = medication.dose,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = catColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = medication.category,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = catColor,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f), CircleShape)
            ) {
                Icon(
                    Icons.Rounded.DeleteOutline,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// -- Empty State --------------------------------------------------------------

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.radialGradient(listOf(PurpleSurface, OffWhite)),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Medication,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = PurpleMid.copy(alpha = 0.5f)
            )
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Sin medicamentos",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Pulsa el botón\u00A0+ para registrar tu primer medicamento.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}