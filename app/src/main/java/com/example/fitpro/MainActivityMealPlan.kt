package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityMealPlan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_meal_plan)

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Handle Meal Buttons
        val btnAll = findViewById<Button>(R.id.btnAll)
        val btnBreakfast = findViewById<Button>(R.id.btnBreakfast)
        val btnLunch = findViewById<Button>(R.id.btnLunch)
        val btnDinner = findViewById<Button>(R.id.btnDinner)

        btnAll.setOnClickListener {
            val intent = Intent(this, HydrationActivity::class.java)
            startActivity(intent)
        }

        btnBreakfast.setOnClickListener {
            val intent = Intent(this, MainActivityBreakfast::class.java)
            startActivity(intent)
        }

        btnLunch.setOnClickListener {
            val intent = Intent(this, MainActivityLunch::class.java)
            startActivity(intent)
        }

        btnDinner.setOnClickListener {
            val intent = Intent(this, MainActivityDinner::class.java)
            startActivity(intent)
        }

        // ✅ Bottom navigation logic
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_meal // Highlight "Meal"

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainHome::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_meal -> {
                    // Already on Meal screen
                    true
                }
                R.id.nav_exercise -> {
                    val intent = Intent(this, MainActivityExercise::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, MainActivityProfile::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}
