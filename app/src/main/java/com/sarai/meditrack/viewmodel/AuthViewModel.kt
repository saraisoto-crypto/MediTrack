package com.sarai.meditrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Estado de la UI de autenticación
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: FirebaseUser? = null
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(AuthUiState(user = auth.currentUser))
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ¿Hay sesión activa?
    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    // ── Login ────────────────────────────────────────────────────────────────
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = auth.currentUser
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = translateError(e.message)
                )
            }
        }
    }

    // ── Registro ─────────────────────────────────────────────────────────────
    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    user = auth.currentUser
                )
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = translateError(e.message)
                )
            }
        }
    }

    // ── Cerrar sesión ─────────────────────────────────────────────────────────
    fun logout(onDone: () -> Unit) {
        auth.signOut()
        _uiState.value = AuthUiState(user = null)
        onDone()
    }

    // ── Limpiar error ─────────────────────────────────────────────────────────
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // ── Traducción de errores Firebase a español ──────────────────────────────
    private fun translateError(message: String?): String {
        return when {
            message == null                               -> "Error desconocido."
            message.contains("email address is already") -> "Este correo ya está registrado."
            message.contains("no user record")           -> "No existe una cuenta con ese correo."
            message.contains("password is invalid")      -> "Contraseña incorrecta."
            message.contains("badly formatted")          -> "El formato del correo no es válido."
            message.contains("network error")            -> "Sin conexión. Verifica tu red."
            message.contains("weak-password")            -> "La contraseña debe tener al menos 6 caracteres."
            else                                          -> "Error: $message"
        }
    }
}