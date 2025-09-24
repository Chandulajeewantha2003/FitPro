package com.example.fitpro

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityExercise : AppCompatActivity() {

    private lateinit var customExerciseContainer: LinearLayout

    private val selectExerciseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedExercises =
                    result.data?.getSerializableExtra("selectedExercises") as? ArrayList<Exercise>

                // 🔹 Clear old and reload new
                customExerciseContainer.removeAllViews()
                selectedExercises?.forEach { exercise ->
                    addExerciseCard(exercise)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_exercise)

        customExerciseContainer = findViewById(R.id.customExerciseContainer)
        val btnAddExercise = findViewById<Button>(R.id.btnAddExercise)

        btnAddExercise.setOnClickListener {
            val intent = Intent(this, MainAllExersizes::class.java)

            // 🔹 Collect currently added exercises
            val currentExercises = arrayListOf<Exercise>()
            for (i in 0 until customExerciseContainer.childCount) {
                val cardView = customExerciseContainer.getChildAt(i)
                val name = cardView.findViewById<TextView>(R.id.tvExerciseName).text.toString()
                val kcalTime = cardView.findViewById<TextView>(R.id.tvKcalTime).text.toString()
                val level = cardView.findViewById<TextView>(R.id.tvLevel).text.toString()
                val imgRes = cardView.findViewById<ImageView>(R.id.imgExercise).tag as? Int ?: 0

                val parts = kcalTime.split("|").map { it.trim() }
                val kcal = parts.getOrNull(0) ?: ""
                val time = parts.getOrNull(1) ?: ""

                currentExercises.add(Exercise(name, kcal, time, level, imgRes))
            }

            // 🔹 Send existing list to MainAllExersizes
            intent.putExtra("selectedExercises", currentExercises)

            selectExerciseLauncher.launch(intent)
        }

        // ✅ Bottom Navigation Setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_exercise // Highlight Exercise tab

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
                R.id.nav_exercise -> true // Already here
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

    private fun addExerciseCard(exercise: Exercise) {
        val cardView = layoutInflater.inflate(R.layout.custom_exercise_card, null)

        val img = cardView.findViewById<ImageView>(R.id.imgExercise)
        val name = cardView.findViewById<TextView>(R.id.tvExerciseName)
        val kcalTime = cardView.findViewById<TextView>(R.id.tvKcalTime)
        val level = cardView.findViewById<TextView>(R.id.tvLevel)
        val btnRemove = cardView.findViewById<Button>(R.id.btnRemoveExercise)

        img.setImageResource(exercise.imageRes)
        img.tag = exercise.imageRes // 🔹 store resource id for reuse
        name.text = exercise.name
        kcalTime.text = "${exercise.kcal} | ${exercise.time}"
        level.text = exercise.level

        btnRemove.setOnClickListener {
            customExerciseContainer.removeView(cardView)
        }

        customExerciseContainer.addView(cardView)
    }
}
