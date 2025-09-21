package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainAllExersizes : AppCompatActivity() {

    private val selectedExercises = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_all_exersizes)

        // Find buttons
        val btnAddExercise1 = findViewById<Button>(R.id.btnAddExercise1)
        val btnAddExercise2 = findViewById<Button>(R.id.btnAddExercise2)
        val btnAddExercise3 = findViewById<Button>(R.id.btnAddExercise3)
        val btnDone = findViewById<Button>(R.id.btnDone)

        // Map buttons to exercise names (match text shown in card)
        val exerciseMap = mapOf(
            btnAddExercise1 to "Exercises with Jumping Rope",
            btnAddExercise2 to "Exercises with Sitting Dumbbells",
            btnAddExercise3 to "Exercises with Holding Jumping Rope"
        )

        // Setup click listeners to toggle selection
        exerciseMap.forEach { (button, name) ->
            button.setOnClickListener {
                if (selectedExercises.contains(name)) {
                    selectedExercises.remove(name)
                    button.setBackgroundColor(resources.getColor(R.color.bg_light_green))
                    button.text = "+"
                } else {
                    selectedExercises.add(name)
                    button.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
                    button.text = "✓"
                }
            }
        }

        // Done button: send selected exercises back to MainActivityExercise
        btnDone.setOnClickListener {
            val intent = Intent().apply {
                putStringArrayListExtra("selectedExercises", ArrayList(selectedExercises))
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
