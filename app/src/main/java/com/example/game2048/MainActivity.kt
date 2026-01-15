package com.example.game2048

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnStatistics: Button
    private lateinit var btnSettings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnPlay = findViewById(R.id.btnPlay)
        btnPlay.setOnClickListener {
            val gameActivity = Intent(this, GameActivity::class.java)
            startActivity(gameActivity)
            }
        btnStatistics = findViewById(R.id.btnStatistics)
        btnStatistics.setOnClickListener {
            val statisticsActivity = Intent(this, StatisticsActivity::class.java)
            startActivity(statisticsActivity)
            }

        btnSettings = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val settingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(settingsActivity)
            }
    }
}