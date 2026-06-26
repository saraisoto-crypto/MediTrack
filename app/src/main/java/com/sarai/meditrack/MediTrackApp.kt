package com.sarai.meditrack

import android.app.Application
import com.sarai.meditrack.data.local.MedicationDatabase
import com.sarai.meditrack.data.repository.MedicationRepository
import com.sarai.meditrack.notifications.NotificationHelper

class MediTrackApp : Application() {

    val database   by lazy { MedicationDatabase.getDatabase(this) }
    val repository by lazy { MedicationRepository(database.medicationDao(), this) }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}