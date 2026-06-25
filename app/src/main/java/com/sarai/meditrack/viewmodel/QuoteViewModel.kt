package com.sarai.meditrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sarai.meditrack.data.repository.MedicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class QuoteUiState(
    val quote: String = "",
    val author: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class QuoteViewModel(
    private val repository: MedicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuoteUiState())
    val uiState: StateFlow<QuoteUiState> = _uiState

    init {
        loadQuote()
    }

    fun loadQuote() {
        viewModelScope.launch {
            _uiState.value = QuoteUiState(isLoading = true)

            val result = repository.getRandomQuote()

            result.fold(
                onSuccess = { quote ->
                    _uiState.value = QuoteUiState(
                        quote = quote.phrase,
                        author = quote.author
                    )
                },
                onFailure = { exception ->
                    _uiState.value = QuoteUiState(
                        error = exception.message ?: "Error al obtener la frase"
                    )
                }
            )
        }
    }

    companion object {
        fun factory(repository: MedicationRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return QuoteViewModel(repository) as T
                }
            }
    }
}