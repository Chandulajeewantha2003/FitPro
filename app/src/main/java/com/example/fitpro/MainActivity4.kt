package com.example.fitpro

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val agePicker: NumberPicker = findViewById(R.id.agePicker)
        val nextBtn: Button = findViewById(R.id.nextBtn)

        agePicker.minValue = 18
        agePicker.maxValue = 100
        agePicker.value = 27

        nextBtn.setOnClickListener {
            val selectedAge = agePicker.value

            prefs.edit().putInt("age", selectedAge).apply()

            val intent = Intent(this, MainActivity5::class.java)
            startActivity(intent)
        }
    }
}
