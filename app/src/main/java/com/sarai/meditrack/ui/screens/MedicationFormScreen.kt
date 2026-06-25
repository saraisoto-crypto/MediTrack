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
import androidx.compose.ui.draw.shadow
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

    var name     by remember { mutableStateOf("") }
    var dose     by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var time     by remember { mutableStateOf("") }
    var notes    by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
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

    val isValid = name.isNotBlank() && dose.isNotBlank()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        Column(modifier = Modifier.fillMaxSize()) {

            // -- Header con gradiente morado --------------------------------
            FormHeader(
                title = if (isEditing) "Editar registro" else "Nuevo registro",
                subtitle = "Completa la información del medicamento",
                onBack = { navController.popBackStack() }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                // -- Sección 1: Identificación --------------------------------
                FormSection(
                    title = "Identificación",
                    icon = Icons.Rounded.Medication,
                    accentColor = Color(0xFFA855F7)
                ) {
                    MediTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Nombre del medicamento",
                        placeholder = "ej: Paracetamol",
                        icon = Icons.Rounded.LocalPharmacy
                    )

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            leadingIcon = {
                                Icon(Icons.Rounded.Category, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            shape = RoundedCornerShape(14.dp),
                            colors = fieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, style = MaterialTheme.typography.bodyMedium) },
                                    onClick = { selectedCategory = cat; expanded = false }
                                )
                            }
                        }
                    }
                }

                // -- Sección 2: Posología -------------------------------------
                FormSection(
                    title = "Posología",
                    icon = Icons.Rounded.Scale,
                    accentColor = Color(0xFFFF4FD8)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(Modifier.weight(1f)) {
                            MediTextField(
                                value = dose,
                                onValueChange = { dose = it },
                                label = "Dosis",
                                placeholder = "ej: 500mg",
                                icon = Icons.Rounded.Scale
                            )
                        }
                        Box(Modifier.weight(1f)) {
                            MediTextField(
                                value = time,
                                onValueChange = { time = it },
                                label = "Hora",
                                placeholder = "ej: 08:00",
                                icon = Icons.Rounded.AccessTime
                            )
                        }
                    }

                    MediTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        label = "Frecuencia",
                        placeholder = "ej: cada 8 horas / una vez al día",
                        icon = Icons.Rounded.Repeat
                    )
                }

                // -- Sección 3: Notas -----------------------------------------
                FormSection(
                    title = "Notas adicionales",
                    icon = Icons.Rounded.Notes,
                    accentColor = Color(0xFF8B5CF6)
                ) {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Instrucciones especiales") },
                        placeholder = { Text("ej: tomar con alimentos, no combinar con alcohol...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        minLines = 3,
                        maxLines = 5,
                        colors = fieldColors()
                    )
                }

                Spacer(Modifier.height(4.dp))

                // -- Botón guardar con gradiente -------------------------------
                Button(
                    onClick = {
                        if (isValid) {
                            viewModel.saveMedication(
                                id = medicationId,
                                name = name,
                                dose = dose,
                                frequency = frequency,
                                time = time,
                                category = selectedCategory,
                                notes = notes
                            )
                            navController.popBackStack()
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .shadow(
                            elevation = if (isValid) 14.dp else 0.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color(0xFFA855F7)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA855F7),
                        disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = if (isValid) Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6D28D9), Color(0xFFA855F7), Color(0xFFFF4FD8))
                                ) else Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, Color.Transparent)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                if (isEditing) Icons.Rounded.Save else Icons.Rounded.Check,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(10.dp))
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
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Cancelar",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

// -- FormHeader con gradiente --------------------------------------------------

@Composable
private fun FormHeader(title: String, subtitle: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF6D28D9), Color(0xFFA855F7)),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        )

        Box(
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.18f), CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}

// -- FormSection ----------------------------------------------------------------

@Composable
private fun FormSection(
    title: String,
    icon: ImageVector,
    accentColor: Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = 0.3.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(20.dp), spotColor = accentColor),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                content = content
            )
        }
    }
}

// -- MediTextField --------------------------------------------------------------

@Composable
fun MediTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    placeholder: String = ""
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) ({ Text(placeholder, style = MaterialTheme.typography.bodyMedium) }) else null,
        leadingIcon = {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        colors = fieldColors()
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
    focusedLabelColor    = MaterialTheme.colorScheme.primary,
    cursorColor          = MaterialTheme.colorScheme.primary,
    unfocusedContainerColor = Color.Transparent,
    focusedContainerColor   = Color.Transparent
)