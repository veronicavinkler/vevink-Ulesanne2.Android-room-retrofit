package com.example.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//implements logic, helps typesafe, room optimize and validate queries
@Dao
interface QuizDao {
    //Küsimused
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Query("SELECT * FROM questions WHERE category = :category")
    suspend fun getQuestionsByCategory(category: String): List<Question>

     @Query("DELETE FROM questions")
     suspend fun clearQuestions()

     //Ajalugu
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertGameResult(result: GameResult)

     @Query("SELECT * FROM game_history ORDER BY id DESC")
     fun getAllGameResults(): Flow<List<GameResult>>

     @Query("SELECT * FROM (SELECT * FROM game_history ORDER BY score DESC, date DESC) GROUP BY category")
     fun getBestResultsByCategory(): Flow<List<GameResult>>
}