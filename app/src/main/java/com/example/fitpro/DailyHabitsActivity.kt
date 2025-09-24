package com.example.fitpro

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

data class Habit(var name: String, var progress: Int = 0)

class DailyHabitsActivity : AppCompatActivity() {

    private val habitList = mutableListOf<Habit>()
    private lateinit var adapter: HabitsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_habits)

        val rvHabits = findViewById<RecyclerView>(R.id.rvHabits)
        val btnAddHabit = findViewById<MaterialButton>(R.id.btnAddHabit)

        // Setup adapter with progress click callback
        adapter = HabitsAdapter(
            habits = habitList,
            onEdit = { position -> showAddEditDialog(habitList[position], position) },
            onDelete = { position ->
                habitList.removeAt(position)
                adapter.notifyItemRemoved(position)
            },
            onProgressClick = { position ->
                val habit = habitList[position]
                if (habit.progress < 100) {
                    habit.progress += 10
                    if (habit.progress > 100) habit.progress = 100
                    adapter.notifyItemChanged(position)
                }
            }
        )

        rvHabits.layoutManager = LinearLayoutManager(this)
        rvHabits.adapter = adapter

        // Add Habit button click
        btnAddHabit.setOnClickListener {
            Toast.makeText(this, "Add Habit clicked!", Toast.LENGTH_SHORT).show()
            showAddEditDialog()
        }
    }

    private fun showAddEditDialog(habit: Habit? = null, position: Int? = null) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (habit == null) "Add Habit" else "Edit Habit")

        val input = EditText(this)
        input.hint = "Enter habit name"
        if (habit != null) input.setText(habit.name)
        builder.setView(input)

        builder.setPositiveButton("Save") { _, _ ->
            val name = input.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Habit name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            if (habit == null) {
                habitList.add(Habit(name))
                adapter.notifyItemInserted(habitList.size - 1)
            } else if (position != null) {
                habit.name = name
                adapter.notifyItemChanged(position)
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
