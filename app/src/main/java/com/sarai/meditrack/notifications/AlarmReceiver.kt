package com.sarai.meditrack.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Toast.makeText(
            context,
            "AlarmReceiver ejecutado",
            Toast.LENGTH_LONG
        ).show()

        Log.d("MediTrackAlarm", "AlarmReceiver disparado!")

        val name = intent.getStringExtra("medication_name") ?: "Medicamento"
        val dose = intent.getStringExtra("medication_dose") ?: ""
        val id = intent.getIntExtra("medication_id", 0)

        Log.d("MediTrackAlarm", "Enviando notificación: $name - $dose")

        NotificationHelper.sendMedicationReminder(
            context = context,
            medicationName = name,
            dose = dose,
            notificationId = id
        )
    }
}