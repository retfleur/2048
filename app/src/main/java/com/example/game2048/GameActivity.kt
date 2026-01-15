package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.Menu
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import androidx.appcompat.app.AlertDialog
import androidx.activity.OnBackPressedCallback

class GameActivity : AppCompatActivity() {

    private lateinit var gameManager: GameManager
    private lateinit var gestureDetector: GestureDetector
    private lateinit var boardView: BoardView
    private lateinit var tvScore: TextView
    private lateinit var tvBestScore: TextView
    private lateinit var btnMenu: Button
    private lateinit var btnNewGame: Button
    private lateinit var musicPlayer: MediaPlayer
    private lateinit var mergeSoundPlayer: MediaPlayer
    private var bestScore = 0
    private var bestMaxTile = 0
    private val prefs by lazy { getSharedPreferences("game_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameManager = GameManager(this)
        gameManager.recalcMaxTile()

        btnMenu = findViewById(R.id.btnMenu)
        btnMenu.setOnClickListener {
            showExitDialog()
        }

        boardView = findViewById(R.id.boardView)
        tvScore = findViewById(R.id.tvScore)
        tvBestScore = findViewById(R.id.tvBestScore)
        btnNewGame = findViewById(R.id.btnNewGame)

        onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    showExitDialog()
                }
            }
        )

        boardView.gameManager = gameManager
        bestScore = prefs.getInt("best_score", 0)
        bestMaxTile = prefs.getInt("best_max_tile", 0)


        musicPlayer = MediaPlayer.create(this, R.raw.music)
        musicPlayer.isLooping = true

        mergeSoundPlayer = MediaPlayer.create(this, R.raw.puck)

        gameManager.onMerge = {
            if (prefs.getBoolean("sound_enabled", true)) {
                mergeSoundPlayer.start()
            }
        }

        updateUI()

        btnNewGame.setOnClickListener {
            showNewGameDialog()
        }

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val diffX = e2.x - (e1?.x ?: 0f)
                val diffY = e2.y - (e1?.y ?: 0f)

                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                        if (diffX > 0) gameManager.move("RIGHT")
                        else gameManager.move("LEFT")
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD) {
                        if (diffY > 0) gameManager.move("DOWN")
                        else gameManager.move("UP")
                    }
                }
                updateUI()
                boardView.invalidate()
                if (!gameManager.hasMoves()) {
                    showGameOverDialog()
                }
                return true
            }
        })

    }
    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("Игра окончена")
            .setMessage("Больше нет возможных ходов.")
            .setCancelable(false)
            .setPositiveButton("Новая игра") { _, _ ->
                gameManager.resetGame()
                updateUI()
                boardView.invalidate()
            }
            .show()
    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Предупреждение")
            .setMessage("Выйти в меню? ")
            .setPositiveButton("Да") { _, _ ->
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    private fun showNewGameDialog() {
        AlertDialog.Builder(this)
            .setTitle("Предупреждение")
            .setMessage("Начать новую игру?")
            .setPositiveButton("Да") { _, _ ->
                gameManager.resetGame()
                updateUI()
                boardView.invalidate()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
    private fun updateUI() {
        tvScore.text = "Счет: ${gameManager.score}"
        tvBestScore.text = "Рекорд: $bestScore"

        if (gameManager.score > bestScore) {
            bestScore = gameManager.score
            prefs.edit().putInt("best_score", bestScore).apply()
        }

        if (gameManager.maxTile > bestMaxTile) {
            bestMaxTile = gameManager.maxTile
            prefs.edit().putInt("best_max_tile", bestMaxTile).apply()
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
    override fun onPause() {
        super.onPause()
        gameManager.saveGame()
        musicPlayer.pause()
    }
    override fun onResume() {
        super.onResume()
        if (prefs.getBoolean("music_enabled", true)) {
            musicPlayer.start()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        musicPlayer.release()
        mergeSoundPlayer.release()
    }
}