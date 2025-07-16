package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeGame()
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameOver by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf("Player X's Turn") }

    fun checkWinner(): String? {
        val lines = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // cols
            listOf(0, 4, 8), listOf(2, 4, 6)                   // diagonals
        )
        for (line in lines) {
            val (a, b, c) = line
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        if (board.none { it.isEmpty() }) return "Draw"
        return null
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 3x3 Grid
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    val index = i * 3 + j
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(80.dp)
                            .border(1.dp, Color.Black)
                            .clickable(enabled = !gameOver && board[index].isEmpty()) {
                                board = board.toMutableList().also { it[index] = currentPlayer }
                                val winner = checkWinner()
                                if (winner != null) {
                                    gameOver = true
                                    result = when (winner) {
                                        "Draw" -> "It's a draw!"
                                        else -> "Player $winner wins!"
                                    }
                                } else {
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    result = "Player ${currentPlayer}'s Turn"
                                }
                            }
                    ) {
                        Text(board[index], fontSize = 32.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(result, fontSize = 20.sp, textAlign = TextAlign.Center)

        if (gameOver) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = {
                board = List(9) { "" }
                currentPlayer = "X"
                gameOver = false
                result = "Player X's Turn"
            }) {
                Text("Play Again")
            }
        }
    }
}
