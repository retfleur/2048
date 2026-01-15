package com.example.game2048

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.SharedPreferences
import android.widget.Button
import android.widget.Switch

class SettingsActivity : AppCompatActivity() {

    private lateinit var btnMenu3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        btnMenu3 = findViewById(R.id.btnMenu3)
        btnMenu3.setOnClickListener {
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val prefs: SharedPreferences =
            getSharedPreferences("game_prefs", MODE_PRIVATE)

        val switchMusic = findViewById<Switch>(R.id.switchMusic)
        val switchSound = findViewById<Switch>(R.id.switchSound)

        // Загружаем сохранённые значения
        switchMusic.isChecked = prefs.getBoolean("music_enabled", true)
        switchSound.isChecked = prefs.getBoolean("sound_enabled", true)

        // Музыка
        switchMusic.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("music_enabled", isChecked).apply()
        }

        // Звуки
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("sound_enabled", isChecked).apply()
        }
    }
}