package com.example.myapplication.data.repository

import com.example.myapplication.data.remote.ApiService
import com.example.myapplication.GameResult
import com.example.myapplication.Question
import com.example.myapplication.QuizDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class QuizRepository (
    private val apiService: ApiService,
    private val quizDao: QuizDao
){
    suspend fun refreshQuestions(category: Int, difficulty: String) {
        val response = apiService.getQuestions(category = category, difficulty = difficulty)
        when (response.responseCode) {
            0 -> {
                if (response.results != null) {
                    val entities = response.results.map { dto ->
                        Question(
                            id = UUID.randomUUID().toString(),
                            category = dto.category,
                            questionText = dto.question,
                            correctAnswer = dto.correctAnswer,
                            wrongAnswer1 = dto.incorrectAnswers?.getOrNull(0) ?: "",
                            wrongAnswer2 = dto.incorrectAnswers?.getOrNull(1) ?: "",
                            wrongAnswer3 = dto.incorrectAnswers?.getOrNull(2) ?: "",
                            difficulty = dto.difficulty
                        )
                    }
                    quizDao.clearQuestions()
                    quizDao.insertQuestions(entities)
                }
            }
            5 -> throw Exception("Rate limit exceeded. Please wait a few seconds.")
            else -> throw Exception("API Error: Code ${response.responseCode}")
        }
    }
    suspend fun getQuestionsFromDb(category: String): List<Question> {
        return quizDao.getQuestionsByCategory(category)
    }

    suspend fun saveGameResult(result: GameResult) {
        quizDao.insertGameResult(result)
    }

    fun getGameHistory(): Flow<List<GameResult>> = quizDao.getAllGameResults()

    fun getBestResults(): Flow<List<GameResult>> = quizDao.getBestResultsByCategory()
}
