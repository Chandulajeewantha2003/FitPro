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

class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main5)

        // Handle system bar padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find views
        val weightInput: EditText = findViewById(R.id.weightInput)
        val radioKg: RadioButton = findViewById(R.id.radioKg)
        val radioLbs: RadioButton = findViewById(R.id.radioLbs)
        val nextBtn: Button = findViewById(R.id.nextBtnWeight)

        // Next button action
        nextBtn.setOnClickListener {
            val weight = weightInput.text.toString().ifEmpty { "0" }
            val unit = if (radioKg.isChecked) "kg" else "lbs"

            // Send data to MainActivity6 (or show toast if you prefer)
            val intent = Intent(this, MainActivity6::class.java)
            intent.putExtra("USER_WEIGHT", "$weight $unit")
            startActivity(intent)
        }
    }
}
