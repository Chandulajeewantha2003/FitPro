package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

data class Exercise(
    val name: String,
    val kcal: String,
    val time: String,
    val level: String,
    val imageRes: Int
) : java.io.Serializable // <-- Important to send via Intent

class MainAllExersizes : AppCompatActivity() {

    private val selectedExercises = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_all_exersizes)

        val btnAddExercise1 = findViewById<Button>(R.id.btnAddExercise1)
        val btnAddExercise2 = findViewById<Button>(R.id.btnAddExercise2)
        val btnAddExercise3 = findViewById<Button>(R.id.btnAddExercise3)
        val btnDone = findViewById<Button>(R.id.btnDone)

        // Define exercises
        val exercise1 = Exercise(
            "Exercises with Jumping Rope",
            "110 kcal", "10 min", "Beginner",
            R.drawable.images_removebg_preview_1__2_
        )
        val exercise2 = Exercise(
            "Exercises with Sitting Dumbbells",
            "110 kcal", "10 min", "Beginner",
            R.drawable.image_6
        )
        val exercise3 = Exercise(
            "Exercises with Holding Jumping Rope",
            "110 kcal", "10 min", "Beginner",
            R.drawable._9694286_xs_removebg_preview_1
        )

        // Map buttons
        val map = mapOf(
            btnAddExercise1 to exercise1,
            btnAddExercise2 to exercise2,
            btnAddExercise3 to exercise3
        )

        map.forEach { (button, exercise) ->
            button.setOnClickListener {
                if (selectedExercises.contains(exercise)) {
                    selectedExercises.remove(exercise)
                    button.text = "+"
                } else {
                    selectedExercises.add(exercise)
                    button.text = "✓"
                }
            }
        }

        btnDone.setOnClickListener {
            val intent = Intent().apply {
                putExtra("selectedExercises", ArrayList(selectedExercises))
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}
