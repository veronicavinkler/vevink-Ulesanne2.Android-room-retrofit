package com.example.myapplication

sealed class Screen(val route: String) {
    object CategorySelection : Screen("category_selection")
    object Quiz : Screen("quiz/{categoryId}/{categoryName}") {
        fun createRoute(id: Int, name: String) = "quiz/$id/$name"
    }
    object Results : Screen("results/{score}/{total}/{category}") {
        fun createRoute(score: Int, total: Int, category: String) = "results/$score/$total/$category"
    }
    object History : Screen("history")
    object Leaderboard : Screen("leaderboard")
}