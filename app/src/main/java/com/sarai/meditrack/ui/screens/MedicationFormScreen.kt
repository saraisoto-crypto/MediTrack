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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarai.meditrack.MediTrackApp
import com.sarai.meditrack.ui.theme.*
import com.sarai.meditrack.viewmodel.MedicationFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationFormScreen(navController: NavController, medicationId: Int) {
    val context = LocalContext.current
    val app = context.applicationContext as MediTrackApp
    val viewModel: MedicationFormViewModel = viewModel(
        factory = MedicationFormViewModel.factory(app.repository)
    )
    val medication by viewModel.medication.collectAsState()
    val isEditing = medicationId != -1

    var name             by remember { mutableStateOf("") }
    var dose             by remember { mutableStateOf("") }
    var frequency        by remember { mutableStateOf("") }
    var time             by remember { mutableStateOf("") }
    var notes            by remember { mutableStateOf("") }
    var expanded         by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Analgésico") }

    val categories = listOf(
        "Analgésico", "Antibiótico", "Vitamina / Suplemento",
        "Antiinflamatorio", "Antiácido", "Antihistamínico", "Otro"
    )

    LaunchedEffect(medicationId) {
        if (isEditing) viewModel.loadMedication(medicationId)
    }
    LaunchedEffect(medication) {
        medication?.let {
            name             = it.name
            dose             = it.dose
            frequency        = it.frequency
            time             = it.time
            notes            = it.notes
            selectedCategory = it.category
        }
    }

    val isValid = name.isNotBlank() && dose.isNotBlank() && time.isNotBlank()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF3A0CA3), Color(0xFF7B2FBE), PinkAccent),
                            start  = Offset(0f, 0f),
                            end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                        )
                    )
            ) {
                // Círculo decorativo
                Box(
                    Modifier.size(140.dp).align(Alignment.TopEnd).offset(x = 40.dp, y = (-30).dp)
                        .background(Color.White.copy(alpha = 0.07f), CircleShape)
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 44.dp, bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.background(Color.White.copy(alpha = 0.18f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver", tint = Color.White)
                    }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(
                            if (isEditing) "Editar medicamento" else "Nuevo medicamento",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            "Completa todos los campos",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        },
        containerColor = BackgroundDark
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Sección 1: Información básica ─────────────────────────────
            FormCard(title = "Identificación", icon = Icons.Rounded.Medication, iconTint = PurpleLight) {
                OutlinedTextField(
                    value         = name,
                    onValueChange = { name = it },
                    label         = { Text("Nombre del medicamento") },
                    placeholder   = { Text("ej: Paracetamol, Ibuprofeno") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(14.dp),
                    leadingIcon   = { Icon(Icons.Rounded.LocalPharmacy, null, tint = PurpleLight) },
                    singleLine    = true,
                    colors        = formFieldColors()
                )

                ExposedDropdownMenuBox(
                    expanded          = expanded,
                    onExpandedChange  = { expanded = !expanded },
                    modifier          = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value         = selectedCategory,
                        onValueChange = {},
                        readOnly      = true,
                        label         = { Text("Categoría") },
                        trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        leadingIcon   = { Icon(Icons.Rounded.Category, null, tint = PurpleLight) },
                        modifier      = Modifier.fillMaxWidth().menuAnchor(),
                        shape         = RoundedCornerShape(14.dp),
                        colors        = formFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded          = expanded,
                        onDismissRequest  = { expanded = false },
                        modifier          = Modifier.background(SurfaceDark)
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text    = { Text(cat, color = TextPrimary) },
                                onClick = { selectedCategory = cat; expanded = false }
                            )
                        }
                    }
                }
            }

            // ── Sección 2: Dosis y horario ────────────────────────────────
            FormCard(title = "Dosis y Horario", icon = Icons.Rounded.Schedule, iconTint = PinkAccent) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value         = dose,
                        onValueChange = { dose = it },
                        label         = { Text("Dosis") },
                        placeholder   = { Text("ej: 500mg") },
                        modifier      = Modifier.weight(1f),
                        shape         = RoundedCornerShape(14.dp),
                        leadingIcon   = { Icon(Icons.Rounded.Scale, null, tint = PurpleLight) },
                        singleLine    = true,
                        colors        = formFieldColors()
                    )
                    OutlinedTextField(
                        value         = time,
                        onValueChange = { time = it },
                        label         = { Text("Hora") },
                        placeholder   = { Text("ej: 08:00") },
                        modifier      = Modifier.weight(1f),
                        shape         = RoundedCornerShape(14.dp),
                        leadingIcon   = { Icon(Icons.Rounded.AccessTime, null, tint = PurpleLight) },
                        singleLine    = true,
                        colors        = formFieldColors()
                    )
                }

                // Aviso de formato 24h
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PurpleSurface
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Rounded.Info, null, tint = PurpleLight, modifier = Modifier.size(14.dp))
                        Text(
                            "Usa formato 24h: 08:00 = 8am · 13:00 = 1pm · 20:00 = 8pm",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                OutlinedTextField(
                    value         = frequency,
                    onValueChange = { frequency = it },
                    label         = { Text("Frecuencia") },
                    placeholder   = { Text("ej: cada 8 horas / una vez al día") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(14.dp),
                    leadingIcon   = { Icon(Icons.Rounded.Repeat, null, tint = PurpleLight) },
                    singleLine    = true,
                    colors        = formFieldColors()
                )
            }

            // ── Sección 3: Notas ──────────────────────────────────────────
            FormCard(title = "Notas adicionales", icon = Icons.Rounded.Notes, iconTint = CyanAccent) {
                OutlinedTextField(
                    value         = notes,
                    onValueChange = { notes = it },
                    label         = { Text("Instrucciones especiales") },
                    placeholder   = { Text("ej: tomar con alimentos, no combinar con alcohol...") },
                    modifier      = Modifier.fillMaxWidth(),
                    shape         = RoundedCornerShape(14.dp),
                    minLines      = 3,
                    maxLines      = 5,
                    colors        = formFieldColors()
                )
            }

            Spacer(Modifier.height(8.dp))

            // ── Botón guardar con gradiente ───────────────────────────────
            Button(
                onClick = {
                    if (isValid) {
                        viewModel.saveMedication(
                            id        = medicationId,
                            name      = name,
                            dose      = dose,
                            frequency = frequency,
                            time      = time,
                            category  = selectedCategory,
                            notes     = notes
                        )
                        navController.popBackStack()
                    }
                },
                enabled           = isValid,
                modifier          = Modifier.fillMaxWidth().height(60.dp),
                shape             = RoundedCornerShape(18.dp),
                contentPadding    = PaddingValues(),
                colors            = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isValid)
                                Brush.linearGradient(listOf(PurpleElectric, PinkAccent))
                            else
                                Brush.linearGradient(listOf(Color(0xFF444444), Color(0xFF333333))),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(
                            if (isEditing) Icons.Rounded.Save else Icons.Rounded.Check,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            if (isEditing) "Guardar cambios" else "Registrar medicamento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            TextButton(
                onClick  = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar", color = TextSecondary)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── FormCard ──────────────────────────────────────────────────────────────────

@Composable
fun FormCard(
    title: String,
    icon: ImageVector,
    iconTint: Color = PurpleLight,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.elevatedCardColors(containerColor = SurfaceDark),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(iconTint.copy(alpha = 0.15f), RoundedCornerShape(9.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = iconTint, modifier = Modifier.size(15.dp))
                }
                Spacer(Modifier.width(10.dp))
                Text(
                    title,
                    style      = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    letterSpacing = 0.3.sp
                )
            }
            content()
        }
    }
}

// ── Campo de texto con estilos dark ──────────────────────────────────────────

@Composable
fun formFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = PurpleElectric,
    unfocusedBorderColor    = OutlineLight,
    focusedLabelColor       = PurpleLight,
    unfocusedLabelColor     = TextSecondary,
    focusedTextColor        = TextPrimary,
    unfocusedTextColor      = TextPrimary,
    cursorColor             = PurpleElectric,
    focusedContainerColor   = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    focusedPlaceholderColor = TextSecondary,
    unfocusedPlaceholderColor = OutlineLight
)

