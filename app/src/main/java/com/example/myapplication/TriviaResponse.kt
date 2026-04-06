package com.example.myapplication

data class TriviaResponse (
    val responseCode: Int,
    val results: List<QuestionDto>
)

data class QuestionDto(
    val category: String,
    val type: String,
    val difficulty: String,
    val question: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>
)