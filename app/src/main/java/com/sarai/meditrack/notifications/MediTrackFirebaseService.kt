package com.sarai.meditrack.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MediTrackFirebaseService : FirebaseMessagingService() {

    private val TAG = "FCMService"

    // ── Llega notificación push con la app abierta ────────────────────────────
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "Mensaje FCM recibido de: ${remoteMessage.from}")

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "MediTrack"

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "Tienes un nuevo recordatorio"

        NotificationHelper.sendMedicationReminder(
            context        = applicationContext,
            medicationName = title,
            dose           = body,
            notificationId = System.currentTimeMillis().toInt()
        )
    }

    // ── Token renovado — guardarlo automáticamente ────────────────────────────
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Token FCM renovado: $token")
        TokenManager.saveToken(applicationContext, token)
    }
}