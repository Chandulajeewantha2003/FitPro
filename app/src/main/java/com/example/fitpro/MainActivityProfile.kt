package com.example.fitpro

import android.content.Intent
import android.widget.EditText
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_profile)

        val prefs = getSharedPreferences("user_data", MODE_PRIVATE)

        val age = prefs.getInt("age", 0)
        val weight = prefs.getString("weight", "")
        val height = prefs.getString("height", "")

        // Find EditText views
        val ageEditText: EditText = findViewById(R.id.edit_age)
        val weightEditText: EditText = findViewById(R.id.edit_weight)
        val heightEditText: EditText = findViewById(R.id.edit_height)

        // Populate values
        if (age > 0) ageEditText.setText(age.toString())
        weightEditText.setText(weight)
        heightEditText.setText(height)

        // Bottom nav
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_profile

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainHome::class.java))
                    finish()
                    true
                }

                R.id.nav_meal -> {
                    startActivity(Intent(this, MainActivityMealPlan::class.java))
                    finish()
                    true
                }

                R.id.nav_exercise -> {
                    startActivity(Intent(this, MainActivityExercise::class.java))
                    finish()
                    true
                }

                R.id.nav_profile -> true
                else -> false
            }
        }
    }
}
