package com.sarai.meditrack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarai.meditrack.ui.theme.*
import com.sarai.meditrack.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun fixedAuthFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedBorderColor = PurpleElectric,
    unfocusedBorderColor = PurpleVibrant,
    focusedLabelColor = PurpleMid,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
    cursorColor = PurpleMid,
    focusedLeadingIconColor = PurpleMid,
    unfocusedLeadingIconColor = PurpleMid
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val uiState by authViewModel.uiState.collectAsState()

    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword    by remember { mutableStateOf(false) }
    var showConfirm     by remember { mutableStateOf(false) }
    var registeredOk    by remember { mutableStateOf(false) }

    val passwordsMatch  = password == confirmPassword
    val isFormValid     = email.isNotBlank() && password.length >= 6 && passwordsMatch

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fondo degradado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(PurpleDeep, PurpleVibrant, Color(0xFF9C27B0)),
                        start  = Offset(0f, 0f),
                        end    = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            Box(Modifier.size(200.dp).align(Alignment.TopEnd).offset(x = 60.dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.05f), CircleShape))
            Box(Modifier.size(100.dp).align(Alignment.BottomStart).offset(x = (-30).dp, y = 30.dp)
                .background(FuchsiaBright.copy(alpha = 0.15f), CircleShape))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar flotante
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 48.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.background(Color.White.copy(alpha = 0.18f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Volver", tint = Color.White)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Crear cuenta",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Únete a MediTrack",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Card formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape  = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Tus datos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Email
                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it.trim() },
                        label         = { Text("Correo electrónico") },
                        leadingIcon   = { Icon(Icons.Rounded.Email, null, tint = PurpleMid) },
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine    = true,
                        colors        = fixedAuthFieldColors()
                    )

                    // Contraseña
                    OutlinedTextField(
                        value         = password,
                        onValueChange = { password = it },
                        label         = { Text("Contraseña (mín. 6 caracteres)") },
                        leadingIcon   = { Icon(Icons.Rounded.Lock, null, tint = PurpleMid) },
                        trailingIcon  = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    if (showPassword) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    null, tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine    = true,
                        colors        = fixedAuthFieldColors()
                    )

                    // Confirmar contraseña
                    OutlinedTextField(
                        value         = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label         = { Text("Confirmar contraseña") },
                        leadingIcon   = { Icon(Icons.Rounded.LockOpen, null, tint = PurpleMid) },
                        trailingIcon  = {
                            IconButton(onClick = { showConfirm = !showConfirm }) {
                                Icon(
                                    if (showConfirm) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                                    null, tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (showConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier      = Modifier.fillMaxWidth(),
                        shape         = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine    = true,
                        isError       = confirmPassword.isNotEmpty() && !passwordsMatch,
                        supportingText = {
                            if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                                Text("Las contraseñas no coinciden", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        colors        = fixedAuthFieldColors()
                    )

                    // Error de Firebase
                    if (uiState.error != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.ErrorOutline, null,
                                    tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                Text(
                                    uiState.error!!,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Confirmación de cuenta creada
                    if (registeredOk) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SuccessSurface
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Rounded.CheckCircle, null,
                                    tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                Text(
                                    "Cuenta creada. Inicia sesión para continuar.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SuccessGreen
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Botón registrar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(
                                if (isFormValid && !uiState.isLoading)
                                    Brush.linearGradient(listOf(PurpleMid, FuchsiaBright))
                                else
                                    Brush.linearGradient(listOf(Color(0xFFCCCCCC), Color(0xFFBBBBBB))),
                                RoundedCornerShape(14.dp)
                            )
                    ) {
                        Button(
                            onClick = {
                                authViewModel.clearError()
                                authViewModel.register(email, password) {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }
                            },
                            enabled  = isFormValid && !uiState.isLoading,
                            modifier = Modifier.fillMaxSize(),
                            shape    = RoundedCornerShape(14.dp),
                            colors   = ButtonDefaults.buttonColors(
                                containerColor         = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            ),
                            elevation = null
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
                            } else {
                                Icon(Icons.Rounded.PersonAdd, null, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Crear cuenta", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Link a login
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta?", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Inicia sesión", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = PurpleMid)
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }
}