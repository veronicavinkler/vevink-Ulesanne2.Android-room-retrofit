package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.QuizUiState
import com.example.myapplication.ui.QuizViewModel

@Composable
fun QuizScreen (
    viewModel: QuizViewModel,
    categoryId: Int,
    categoryName: String,
    onQuizFinished: (Int, Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchQuestions(categoryId, categoryName, "medium")
    }
    when (val state = uiState) {
        is QuizUiState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is QuizUiState.Error -> Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(state.massage, modifier = Modifier.padding(16.dp))
            Button(onClick = { viewModel.fetchQuestions(categoryId, categoryName, "medium") }) {
                Text("Proovi uuesti")
            }
        }

        is QuizUiState.Success -> {
            val questions = state.questions
            if (currentQuestionIndex < questions.size) {
                val question = questions[currentQuestionIndex]
                val answers = remember(question) {
                    listOf(
                        question.correctAnswer,
                        question.wrongAnswer1,
                        question.wrongAnswer2,
                        question.wrongAnswer3
                    ).shuffled()
                }
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "Küsimus ${currentQuestionIndex + 1}/${questions.size}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(question.questionText, style = MaterialTheme.typography.headlineSmall)

                    answers.forEach { answer ->
                        Button(
                            onClick = {
                                if (answer == question.correctAnswer) score++
                                if (currentQuestionIndex + 1 < questions.size) {
                                    currentQuestionIndex++
                                } else {
                                    viewModel.saveResult(score, questions.size, categoryName)
                                    onQuizFinished(score, questions.size)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) { Text(answer) }
                    }
                }
            }
        }
    }
}