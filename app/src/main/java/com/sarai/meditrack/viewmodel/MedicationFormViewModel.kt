package com.sarai.meditrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sarai.meditrack.data.local.Medication
import com.sarai.meditrack.data.repository.MedicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MedicationFormViewModel(
    private val repository: MedicationRepository
) : ViewModel() {

    private val _medication = MutableStateFlow<Medication?>(null)
    val medication: StateFlow<Medication?> = _medication

    fun loadMedication(id: Int) {
        viewModelScope.launch {
            _medication.value = repository.getMedicationById(id)
        }
    }

    fun saveMedication(
        id: Int,
        name: String,
        dose: String,
        frequency: String,
        time: String,
        category: String,
        notes: String
    ) {
        viewModelScope.launch {
            val medication = Medication(
                id = if (id == -1) 0 else id,
                name = name,
                dose = dose,
                frequency = frequency,
                time = time,
                category = category,
                notes = notes
            )
            if (id == -1) {
                repository.insertMedication(medication)
            } else {
                repository.updateMedication(medication)
            }
        }
    }

    companion object {
        fun factory(repository: MedicationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MedicationFormViewModel(repository) as T
                }
            }
    }
}