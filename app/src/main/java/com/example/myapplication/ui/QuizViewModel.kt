package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.GameResult
import com.example.myapplication.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    val history = repository.getGameHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestResults = repository.getBestResults()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState

    fun fetchQuestions(categoryId: Int, categoryName: String, difficulty: String) {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                repository.refreshQuestions(categoryId, difficulty)
                val questions = repository.getQuestionsFromDb(categoryName)
                if (questions.isNotEmpty()) {
                    _uiState.value = QuizUiState.Success(questions)
                } else {
                    _uiState.value = QuizUiState.Error("Küsimusi ei leitud.")
                }
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("Rate limit") == true -> "Liiga palju päringuid. Palun oota paar sekundit."
                    else -> "Viga andmete laadimisel: ${e.localizedMessage ?: "Kontrolli internetti"}"
                }
                _uiState.value = QuizUiState.Error(errorMessage)
            }
        }
    }

    fun saveResult(score: Int, total: Int, category: String) {
        viewModelScope.launch {
            val result = GameResult(
                date = SimpleDateFormat
                    (
                    "dd.MM.yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date()),
                category = category,
                score = score,
                totalQuestions = total
            )
            repository.saveGameResult(result)
        }
    }
}