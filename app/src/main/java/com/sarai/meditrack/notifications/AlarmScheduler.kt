package com.sarai.meditrack.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sarai.meditrack.data.local.Medication
import java.util.Calendar

object AlarmScheduler {

    // ── Programar alarma diaria para un medicamento ───────────────────────────
    fun scheduleAlarm(context: Context, medication: Medication) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("medication_name", medication.name)
            putExtra("medication_dose", medication.dose)
            putExtra("medication_id",  medication.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Parsear "HH:mm" del campo time
        val parts = medication.time.trim().split(":")
        if (parts.size != 2) return
        val hour   = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        // PRUEBA: dispara en 1 minuto
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        }

        // Alarma que se repite cada día a la misma hora
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    // ── Cancelar alarma de un medicamento ─────────────────────────────────────
    fun cancelAlarm(context: Context, medicationId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medicationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}