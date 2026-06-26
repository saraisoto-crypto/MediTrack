package com.sarai.meditrack.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MediTrackFirebaseService : FirebaseMessagingService() {

    // ── Se llama cuando llega una notificación push con la app en primer plano ─
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "MediTrack"

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "Tienes un nuevo recordatorio"

        // Mostrar como notificación local cuando la app está abierta
        NotificationHelper.sendMedicationReminder(
            context          = applicationContext,
            medicationName   = title,
            dose             = body,
            notificationId   = System.currentTimeMillis().toInt()
        )
    }

    // ── Se llama cuando Firebase renueva el token del dispositivo ─────────────
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // En una app real, aquí se enviaría el token al servidor
        // Para el proyecto, Firebase lo gestiona automáticamente
    }
}