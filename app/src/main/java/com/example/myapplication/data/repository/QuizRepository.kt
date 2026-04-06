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
        try {
            val response = apiService.getQuestions(category = category, difficulty = difficulty)
            if (response.responseCode == 0) {
                val entities = response.results.map { dto ->
                    Question(
                        id = UUID.randomUUID().toString(),
                        category = dto.category,
                        difficulty = dto.difficulty,
                        questionText = dto.question,
                        correctAnswer = dto.correctAnswer,
                        wrongAnswer1 = dto.incorrectAnswers[0],
                        wrongAnswer2 = dto.incorrectAnswers[1],
                        wrongAnswer3 = dto.incorrectAnswers[2]
                    )
                }
                quizDao.clearQuestions()
                quizDao.insertQuestions(entities)
            }
        } catch (e: Exeption) {
            e.printStackTrace()
        }
    }
    suspend fun getQuestionsFromDb(category: String): List<Question> {
        return quizDao.getQuestionsByCategory(category)
    }

    suspend fun saveGameResult(result: GameResult) {
        quizDao.insertGameResult(result)
    }

    suspend fun getGameHistory(): Flow<List<GameResult>> = quizDao.getAllGameResults()
}
