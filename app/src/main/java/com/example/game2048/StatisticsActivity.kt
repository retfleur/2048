package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AlertDialog

class StatisticsActivity : AppCompatActivity() {

    private lateinit var tvBestScoreStat: TextView
    private lateinit var tvMaxTile: TextView
    private lateinit var btnResetStats: Button
    private lateinit var btnMenu2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statistics)

        btnMenu2 = findViewById(R.id.btnMenu2)
        btnMenu2.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statisticsLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)

        tvMaxTile = findViewById(R.id.tvMaxTile)

        val bestMaxTile = prefs.getInt("best_max_tile", 0)
        tvMaxTile.text = "Максимальная плитка: $bestMaxTile"


        tvBestScoreStat = findViewById(R.id.tvBestScoreStat)
        btnResetStats = findViewById(R.id.btnResetStats)

        updateBestScore()

        btnResetStats.setOnClickListener {
            showResetStatsDialog()
        }
    }

    private fun showResetStatsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Предупреждение")
            .setMessage("Сбросить рекорд?")
            .setPositiveButton("Да") { _, _ ->
                val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putInt("best_score", 0)
                    .putInt("best_max_tile", 0)
                    .apply()
                updateBestScore()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        updateBestScore()
    }
    private fun updateBestScore() {
        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)

        val bestScore = prefs.getInt("best_score", 0)
        val bestMaxTile = prefs.getInt("best_max_tile", 0)

        tvBestScoreStat.text = "Рекорд: $bestScore"
        tvMaxTile.text = "Максимальная плитка: $bestMaxTile"
    }
}
