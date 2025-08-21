package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity6 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main6)

        // Handle system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find views
        val heightInput: EditText = findViewById(R.id.heightInput)
        val radioCm: RadioButton = findViewById(R.id.radioCm)
        val radioFt: RadioButton = findViewById(R.id.radioFt)
        val nextBtn: Button = findViewById(R.id.nextBtnHeight)

        // Next button action
        nextBtn.setOnClickListener {
            val height = heightInput.text.toString().ifEmpty { "0" }
            val unit = if (radioCm.isChecked) "cm" else "ft"

            // Example: send data to MainActivity7
            val intent = Intent(this, MainActivity7::class.java)
            intent.putExtra("USER_HEIGHT", "$height $unit")
            startActivity(intent)
        }
    }
}
