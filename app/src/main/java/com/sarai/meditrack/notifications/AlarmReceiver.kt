package com.sarai.meditrack.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("medication_name") ?: "Medicamento"
        val dose = intent.getStringExtra("medication_dose") ?: ""
        val id   = intent.getIntExtra("medication_id", 0)

        NotificationHelper.sendMedicationReminder(
            context          = context,
            medicationName   = name,
            dose             = dose,
            notificationId   = id
        )
    }
}