package com.sarai.meditrack.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.sarai.meditrack.MainActivity
import com.sarai.meditrack.R

object NotificationHelper {

    private const val CHANNEL_ID   = "meditrack_recordatorios"
    private const val CHANNEL_NAME = "Recordatorios de medicamentos"
    private const val CHANNEL_DESC = "Notificaciones para recordar tomar tus medicamentos a tiempo"

    // ── Crear canal (llamar una sola vez al iniciar la app) ───────────────────
    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = CHANNEL_DESC
            enableVibration(true)
            enableLights(true)
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    // ── Enviar notificación local ─────────────────────────────────────────────
    fun sendMedicationReminder(
        context: Context,
        medicationName: String,
        dose: String,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⏰ Hora de tu medicamento")
            .setContentText("$medicationName — $dose")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Es hora de tomar $medicationName ($dose). ¡No olvides tu salud!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }

    // ── Cancelar una notificación específica ──────────────────────────────────
    fun cancelNotification(context: Context, notificationId: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(notificationId)
    }
}