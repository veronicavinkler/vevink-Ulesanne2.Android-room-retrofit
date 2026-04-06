package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.GameResult
import com.example.myapplication.data.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState

    fun fetchQuestions(categoryId: Int, categoryName: String, difficulty: String) {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            repository.refreshQuestions(categoryId, difficulty)
            val questions = repository.getQuestionsFromDb(categoryName)
            if (questions.isNotEmpty()) {
                _uiState.value = QuizUiState.Success(questions)
            } else {
                _uiState.value = QuizUiState.Error("Küsimusi ei leitud, Kontrolli interneti")
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