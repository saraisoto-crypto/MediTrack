package com.sarai.meditrack.data.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sarai.meditrack.data.local.Medication
import com.sarai.meditrack.data.local.MedicationDao
import com.sarai.meditrack.data.remote.QuoteResponse
import com.sarai.meditrack.data.remote.RetrofitInstance
import com.sarai.meditrack.notifications.AlarmScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class MedicationRepository(
    private val dao: MedicationDao,
    private val context: Context
) {

    // ── Firestore ─────────────────────────────────────────────────────────────
    private val firestore = FirebaseFirestore.getInstance()
    private val auth      = FirebaseAuth.getInstance()

    private fun medicationsCollection() =
        firestore
            .collection("users")
            .document(auth.currentUser?.uid ?: "anonymous")
            .collection("medications")

    private fun Medication.toMap(): Map<String, Any> = mapOf(
        "id"        to id,
        "name"      to name,
        "dose"      to dose,
        "frequency" to frequency,
        "time"      to time,
        "category"  to category,
        "notes"     to notes
    )

    // ── Room ──────────────────────────────────────────────────────────────────
    fun getAllMedications(): Flow<List<Medication>> = dao.getAllMedications()

    suspend fun getMedicationById(id: Int): Medication? = dao.getMedicationById(id)

    // ── Insert ────────────────────────────────────────────────────────────────
    suspend fun insertMedication(medication: Medication) {
        val newId = dao.insertMedication(medication)
        val withId = medication.copy(id = newId.toInt())

        // Programar alarma automática a la hora del medicamento
        AlarmScheduler.scheduleAlarm(context, withId)

        // Sincronizar con Firestore
        try {
            medicationsCollection()
                .document(newId.toString())
                .set(withId.toMap())
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ── Update ────────────────────────────────────────────────────────────────
    suspend fun updateMedication(medication: Medication) {
        dao.updateMedication(medication)

        // Reprogramar alarma con la nueva hora
        AlarmScheduler.scheduleAlarm(context, medication)

        try {
            medicationsCollection()
                .document(medication.id.toString())
                .set(medication.toMap())
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────
    suspend fun deleteMedication(medication: Medication) {
        dao.deleteMedication(medication)

        // Cancelar la alarma del medicamento eliminado
        AlarmScheduler.cancelAlarm(context, medication.id)

        try {
            medicationsCollection()
                .document(medication.id.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // ── Frases ────────────────────────────────────────────────────────────────
    suspend fun getRandomQuote(): Result<QuoteResponse> {
        return try {
            val quote = RetrofitInstance.api.getRandomQuote()
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}