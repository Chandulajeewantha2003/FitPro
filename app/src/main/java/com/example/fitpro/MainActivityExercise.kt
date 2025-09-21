package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityExercise : AppCompatActivity() {

    private lateinit var customExerciseContainer: LinearLayout

    // ✅ Launcher to get result from MainAllExercises
    private val pickExerciseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val exerciseName = result.data?.getStringExtra("exerciseName")
                exerciseName?.let { addExerciseCard(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_exercise)

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        customExerciseContainer = findViewById(R.id.customExerciseContainer)
        val btnAddExercise = findViewById<Button>(R.id.btnAddExercise)

        // ✅ Open MainAllExercises instead of adding dummy card
        btnAddExercise.setOnClickListener {
            val intent = Intent(this, MainAllExersizes::class.java)
            pickExerciseLauncher.launch(intent)
        }

        // ✅ Bottom navigation setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_exercise

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainHome::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_meal -> {
                    startActivity(Intent(this, MainActivityMealPlan::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_exercise -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, MainActivityProfile::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    // ✅ Add exercise card dynamically
    private fun addExerciseCard(name: String) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.custom_exercise_card, customExerciseContainer, false)

        val tvExerciseName = cardView.findViewById<TextView>(R.id.tvExerciseName)
        val btnDelete = cardView.findViewById<Button>(R.id.btnDelete)

        tvExerciseName.text = name

        btnDelete.setOnClickListener {
            customExerciseContainer.removeView(cardView)
        }

        customExerciseContainer.addView(cardView)
    }
}
