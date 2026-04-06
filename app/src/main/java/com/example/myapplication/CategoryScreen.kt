package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onCategorySelected: (Int, String) -> Unit,
    onHistoryClick: () -> Unit,
    onLeaderboardClick: () -> Unit
) {
    val categories = listOf(
        9 to "General Knowledge",
        18 to "Science: Computers",
        23 to "History",
        21 to "Sports"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vali kategooria") },
                actions = {
                    IconButton(onClick = onLeaderboardClick) {
                        Icon(Icons.Default.Leaderboard, contentDescription = "Edetabel")
                    }
                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Default.History, contentDescription = "Ajalugu")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(categories) { (id, name) ->
                Button(
                    onClick = { onCategorySelected(id, name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(text = name)
                }
            }
        }
    }
}