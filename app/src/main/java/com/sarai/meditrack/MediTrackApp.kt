package com.sarai.meditrack

import android.app.Application
import com.sarai.meditrack.data.local.MedicationDatabase
import com.sarai.meditrack.data.repository.MedicationRepository
import com.sarai.meditrack.notifications.NotificationHelper
import com.sarai.meditrack.notifications.TokenManager

class MediTrackApp : Application() {

    val database   by lazy { MedicationDatabase.getDatabase(this) }
    val repository by lazy { MedicationRepository(database.medicationDao(), this) }

    override fun onCreate() {
        super.onCreate()
        // Canal de notificaciones locales
        NotificationHelper.createNotificationChannel(this)
        // Obtener y guardar token FCM al iniciar
        TokenManager.fetchAndSaveToken(this)
    }
}