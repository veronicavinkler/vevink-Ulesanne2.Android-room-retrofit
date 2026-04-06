package com.example.myapplication.ui

import com.example.myapplication.Question

sealed class QuizUiState {
    object Loading : QuizUiState()
    data class Success(val questions : List<Question>) : QuizUiState()
    data class Error(val massage: String) : QuizUiState()
}