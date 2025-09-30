package com.example.fitpro

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity5 : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val weightInput: EditText = findViewById(R.id.weightInput)
        val radioKg: RadioButton = findViewById(R.id.radioKg)
        val radioLbs: RadioButton = findViewById(R.id.radioLbs)
        val nextBtn: Button = findViewById(R.id.nextBtnWeight)

        nextBtn.setOnClickListener {
            val weight = weightInput.text.toString().ifEmpty { "0" }
            val unit = if (radioKg.isChecked) "kg" else "lbs"

            prefs.edit().putString("weight", "$weight $unit").apply()

            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
    }
}
