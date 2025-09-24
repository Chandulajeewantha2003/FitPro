package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// ✅ Data class for Exercises
data class Exercise(
    val name: String,
    val kcal: String,
    val time: String,
    val level: String,
    val imageRes: Int
) : java.io.Serializable

class MainAllExersizes : AppCompatActivity() {

    private val selectedExercises = mutableListOf<Exercise>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_all_exersizes)

        // ✅ Receive previous exercises (so ✓ state is correct)
        val previousList =
            intent.getSerializableExtra("selectedExercises") as? ArrayList<Exercise>
        previousList?.let { selectedExercises.addAll(it) }

        // ✅ Get all buttons
        val btnAddExercise1 = findViewById<Button>(R.id.btnAddExercise1)
        val btnAddExercise2 = findViewById<Button>(R.id.btnAddExercise2)
        val btnAddExercise3 = findViewById<Button>(R.id.btnAddExercise3)
        val btnAddExercise4 = findViewById<Button>(R.id.btnAddExercise4)
        val btnAddExercise5 = findViewById<Button>(R.id.btnAddExercise5)
        val btnAddExercise6 = findViewById<Button>(R.id.btnAddExercise6)
        val btnDone = findViewById<Button>(R.id.btnDone)

        // ✅ Define exercises
        val exercise1 = Exercise("Exercises with Jumping Rope","110 kcal","10 min","Beginner", R.drawable.images_removebg_preview_1__2_)
        val exercise2 = Exercise("Exercises with Sitting Dumbbells","110 kcal","10 min","Beginner", R.drawable.image_6)
        val exercise3 = Exercise("Exercises with Holding Jumping Rope","110 kcal","10 min","Beginner", R.drawable._9694286_xs_removebg_preview_1)
        val exercise4 = Exercise("Exercises with Holding Jumping Rope","110 kcal","10 min","Beginner", R.drawable.image_6__1_)
        val exercise5 = Exercise("Exercises with Holding Jumping Rope","110 kcal","10 min","Beginner", R.drawable.group_56536)
        val exercise6 = Exercise("Exercises with Holding Jumping Rope","110 kcal","10 min","Beginner", R.drawable.group_56538)

        // ✅ Map buttons to exercises
        val map = mapOf(
            btnAddExercise1 to exercise1,
            btnAddExercise2 to exercise2,
            btnAddExercise3 to exercise3,
            btnAddExercise4 to exercise4,
            btnAddExercise5 to exercise5,
            btnAddExercise6 to exercise6
        )

        // ✅ Handle add/remove logic
        map.forEach { (button, exercise) ->
            if (selectedExercises.contains(exercise)) {
                button.text = "✓"
            }
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

        // ✅ DONE button returns updated list
        btnDone.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("selectedExercises", ArrayList(selectedExercises))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
