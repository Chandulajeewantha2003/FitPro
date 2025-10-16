package com.example.fitpro

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity6 : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val heightInput: EditText = findViewById(R.id.heightInput)
        val radioCm: RadioButton = findViewById(R.id.radioCm)
        val radioFt: RadioButton = findViewById(R.id.radioFt)
        val nextBtn: Button = findViewById(R.id.nextBtnHeight)

        nextBtn.setOnClickListener {
            val heightText = heightInput.text.toString().trim()
            val heightValue = heightText.toIntOrNull()
            val isCm = radioCm.isChecked
            val valid = if (isCm) heightValue != null && heightValue in 80..250 else heightValue != null && heightValue in 3..8
            if (!valid) {
                Toast.makeText(this, if (isCm) "Enter valid height (80-250 cm)" else "Enter valid height (3-8 ft)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val unit = if (isCm) "cm" else "ft"
            prefs.edit().putString("height", "$heightValue $unit").apply()
            startActivity(Intent(this, MainActivity7::class.java))
        }
    }
}
