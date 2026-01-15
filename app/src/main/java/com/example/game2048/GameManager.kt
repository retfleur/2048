package com.example.game2048

import kotlin.random.Random
import android.content.Context
import android.content.SharedPreferences

class GameManager(private val context: Context, val size: Int = 4) {
    val board = Array(size) { IntArray(size) { 0 } }
    var score = 0
    var maxTile = 0
    var onMerge: (() -> Unit)? = null
    private val prefs: SharedPreferences =
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    init {
        if (!loadGame()) {
            addRandomTile()
            addRandomTile()
        }
    }

    fun resetGame() {
        prefs.edit().remove("current_score").apply()

        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = 0
            }
        }
        score = 0
        addRandomTile()
        addRandomTile()
    }

    private fun addRandomTile() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == 0) emptyCells.add(i to j)
            }
        }
        if (emptyCells.isNotEmpty()) {
            val (x, y) = emptyCells.random()
            board[x][y] = if (Random.nextFloat() < 0.9) 2 else 4
        }
    }

    fun moveLeft(): Boolean {
        var moved = false
        for (i in 0 until size) {
            val newRow = board[i].filter { it != 0 }.toMutableList()
            for (j in 0 until newRow.size - 1) {
                if (newRow[j] == newRow[j + 1]) {
                    newRow[j] *= 2
                    score += newRow[j]

                    if (newRow[j] > maxTile) {
                        maxTile = newRow[j]
                    }
                    newRow[j + 1] = 0
                    onMerge?.invoke()
                }
            }
            val merged = newRow.filter { it != 0 }.toMutableList()
            while (merged.size < size) merged.add(0)
            if (!board[i].contentEquals(merged.toIntArray())) moved = true
            board[i] = merged.toIntArray()
        }
        if (moved) addRandomTile()
        return moved
    }

    fun rotateBoard() {
        val newBoard = Array(size) { IntArray(size) }
        for (i in 0 until size) {
            for (j in 0 until size) {
                newBoard[i][j] = board[j][size - 1 - i]
            }
        }
        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = newBoard[i][j]
            }
        }
    }

    fun move(direction: String): Boolean {
        var moved = false
        when (direction) {
            "LEFT" -> moved = moveLeft()
            "RIGHT" -> {
                rotateBoard(); rotateBoard()
                moved = moveLeft()
                rotateBoard(); rotateBoard()
            }
            "UP" -> {
                rotateBoard()
                moved = moveLeft()
                rotateBoard(); rotateBoard(); rotateBoard()
            }
            "DOWN" -> {
                rotateBoard(); rotateBoard(); rotateBoard()
                moved = moveLeft()
                rotateBoard();
            }
        }
        return moved
    }

    fun saveGame() {
        val editor = prefs.edit()

        // сохраняем счет
        editor.putInt("current_score", score)

        // сохраняем поле
        for (i in 0 until size) {
            for (j in 0 until size) {
                editor.putInt("cell_${i}_$j", board[i][j])
            }
        }

        editor.apply()
    }
    fun loadGame(): Boolean {
        // если нет сохранённого счёта — значит игры нет
        if (!prefs.contains("current_score")) return false

        score = prefs.getInt("current_score", 0)

        for (i in 0 until size) {
            for (j in 0 until size) {
                board[i][j] = prefs.getInt("cell_${i}_$j", 0)
            }
        }
        return true
    }
    fun recalcMaxTile() {
        var max = 0
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] > max) {
                    max = board[i][j]
                }
            }
        }
        maxTile = max
    }
    fun hasMoves(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (board[i][j] == 0) return true

                if (j < size - 1 && board[i][j] == board[i][j + 1]) return true
                if (i < size - 1 && board[i][j] == board[i + 1][j]) return true
            }
        }
        return false
    }
}