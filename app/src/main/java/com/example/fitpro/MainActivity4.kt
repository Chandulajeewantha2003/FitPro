package com.example.fitpro

import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)

        // Handle window insets (status/navigation bar padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find views
        val agePicker: NumberPicker = findViewById(R.id.agePicker)
        val nextBtn: Button = findViewById(R.id.nextBtn)

        // Configure NumberPicker
        agePicker.minValue = 18
        agePicker.maxValue = 100
        agePicker.value = 27   // default value
        agePicker.wrapSelectorWheel = false

        // Handle button click
        nextBtn.setOnClickListener {
            val selectedAge = agePicker.value
            Toast.makeText(this, "Selected Age: $selectedAge", Toast.LENGTH_SHORT).show()
        }
    }
}
