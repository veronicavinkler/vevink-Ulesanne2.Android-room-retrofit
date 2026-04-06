package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.remote.RetrofitClient
import com.example.myapplication.data.repository.QuizRepository
import com.example.myapplication.ui.QuizViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(applicationContext)
        val repository = QuizRepository(RetrofitClient.apiService, db.quizDao())
        val viewModel = QuizViewModel(repository)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Screen.CategorySelection.route) {
                composable(Screen.CategorySelection.route) {
                    CategoryScreen(
                        onCategorySelected = { id, name ->
                            navController.navigate(Screen.Quiz.createRoute(id, name))
                        },
                        onHistoryClick = {
                            navController.navigate(Screen.History.route)
                        },
                        onLeaderboardClick = {
                            navController.navigate(Screen.Leaderboard.route)
                        }
                    )
                }
                composable(Screen.History.route) {
                    HistoryScreen(viewModel) { navController.popBackStack() }
                }
                composable(Screen.Leaderboard.route) {
                    LeaderboardScreen(viewModel) { navController.popBackStack() }
                }
                composable(
                    route = Screen.Quiz.route,
                    arguments = listOf (
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("categoryName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("categoryId") ?: 9
                    val name = backStackEntry.arguments?.getString("categoryName") ?: ""
                    QuizScreen(viewModel, id, name) { score, total ->
                        navController.navigate(Screen.Results.createRoute(score, total, name))
                    }
                }
                composable(Screen.Results.route) { backStackEntry ->
                    val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
                    val total = backStackEntry.arguments?.getString("total")?.toInt() ?: 0
                    ResultScreen(score, total) {
                        navController.navigate(Screen.CategorySelection.route) {
                            popUpTo(Screen.CategorySelection.route) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}