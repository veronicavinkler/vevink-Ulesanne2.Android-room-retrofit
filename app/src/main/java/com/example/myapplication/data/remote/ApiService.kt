package com.example.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

// Declares the HTTP request shape
interface ApiService {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String = "medium",
        @Query("type") type: String = "multiple"
    ): TriviaResponse
}