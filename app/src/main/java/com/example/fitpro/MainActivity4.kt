package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main4)

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val agePicker: NumberPicker = findViewById(R.id.agePicker)
        val nextBtn: Button = findViewById(R.id.nextBtn)

        // Setup NumberPicker
        agePicker.minValue = 18
        agePicker.maxValue = 100
        agePicker.value = 27
        agePicker.wrapSelectorWheel = false

        // On NEXT button click → open MainActivity5
        nextBtn.setOnClickListener {
            val selectedAge = agePicker.value

            // Send selected age to next activity (optional)
            val intent = Intent(this, MainActivity5::class.java)
            intent.putExtra("USER_AGE", selectedAge)
            startActivity(intent)
        }
    }
}
