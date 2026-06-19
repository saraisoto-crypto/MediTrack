package com.sarai.meditrack.data.repository

import com.sarai.meditrack.data.local.Medication
import com.sarai.meditrack.data.local.MedicationDao
import com.sarai.meditrack.data.remote.QuoteResponse
import com.sarai.meditrack.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class MedicationRepository(private val dao: MedicationDao) {

    fun getAllMedications(): Flow<List<Medication>> = dao.getAllMedications()

    suspend fun getMedicationById(id: Int): Medication? = dao.getMedicationById(id)

    suspend fun insertMedication(medication: Medication) = dao.insertMedication(medication)

    suspend fun updateMedication(medication: Medication) = dao.updateMedication(medication)

    suspend fun deleteMedication(medication: Medication) = dao.deleteMedication(medication)

    suspend fun getRandomQuote(): Result<QuoteResponse> {
        return try {
            val quote = RetrofitInstance.api.getRandomQuote()
            Result.success(quote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}