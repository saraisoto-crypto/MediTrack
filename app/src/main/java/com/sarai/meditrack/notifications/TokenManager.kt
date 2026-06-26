package com.sarai.meditrack.notifications

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object TokenManager {

    private const val PREFS_NAME = "meditrack_prefs"
    private const val KEY_TOKEN  = "fcm_token"
    private const val TAG        = "TokenManager"

    // ── Obtener y guardar el token FCM ────────────────────────────────────────
    fun fetchAndSaveToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Error obteniendo token FCM", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            saveToken(context, token)
            Log.d(TAG, "Token FCM obtenido: $token")
        }
    }

    // ── Guardar token en SharedPreferences ────────────────────────────────────
    fun saveToken(context: Context, token: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_TOKEN, token)
            .apply()
    }

    // ── Leer token guardado ───────────────────────────────────────────────────
    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }
}