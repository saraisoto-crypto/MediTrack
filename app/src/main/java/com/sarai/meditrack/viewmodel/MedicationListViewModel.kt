package com.sarai.meditrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sarai.meditrack.data.local.Medication
import com.sarai.meditrack.data.repository.MedicationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedicationListViewModel(
    private val repository: MedicationRepository
) : ViewModel() {

    // Todas las medicaciones de Room
    private val _allMedications: StateFlow<List<Medication>> = repository
        .getAllMedications()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtro seleccionado (null = mostrar todas)
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Lista filtrada que observa la UI
    val medications: StateFlow<List<Medication>> = combine(
        _allMedications,
        _selectedCategory
    ) { all, category ->
        if (category == null) all
        else all.filter { it.category == category }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Categorías disponibles (solo las que tienen medicamentos)
    val availableCategories: StateFlow<List<String>> = _allMedications
        .map { list -> list.map { it.category }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(category: String?) {
        _selectedCategory.value = category
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            repository.deleteMedication(medication)
        }
    }

    companion object {
        fun factory(repository: MedicationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MedicationListViewModel(repository) as T
                }
            }
    }
}