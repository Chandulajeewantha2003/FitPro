package com.example.fitpro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainHome : AppCompatActivity() {

    private lateinit var habitContainer: LinearLayout
    private lateinit var completedContainer: LinearLayout
    private lateinit var buttonAll: Button
    private lateinit var buttonMorning: Button
    private lateinit var buttonAfternoon: Button
    private lateinit var buttonEvening: Button

    companion object {
        val habitList = mutableListOf<Habit>()
    }

    data class Habit(
        val name: String,
        val icon: String,
        val color: Int,
        val date: String,
        val timeOfDay: String,
        val exactTime: String = "", // HH:mm format
        var completed: Boolean = false,
        var progress: Int = 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_home)

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        habitContainer = findViewById(R.id.habitContainer)
        completedContainer = findViewById(R.id.completedContainer)

        // Time-of-day buttons
        buttonAll = findViewById(R.id.buttonAll)
        buttonMorning = findViewById(R.id.buttonMorning)
        buttonAfternoon = findViewById(R.id.buttonAfternoon)
        buttonEvening = findViewById(R.id.buttonEvening)
        setupTimeOfDayButtons()

        // Bottom navigation
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_meal -> {
                    startActivity(Intent(this, MainActivityMealPlan::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_exercise -> {
                    startActivity(Intent(this, MainActivityExercise::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, MainActivityProfile::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }

        // Floating action button to add habit
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, MainActivityCreateHabbits::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        showAllHabits()
    }

    // =========================
    // Time-of-Day Button Logic
    // =========================
    private fun setupTimeOfDayButtons() {
        buttonAll.setOnClickListener { showAllHabits() }
        buttonMorning.setOnClickListener { filterHabits("Morning") }
        buttonAfternoon.setOnClickListener { filterHabits("Afternoon") }
        buttonEvening.setOnClickListener { filterHabits("Evening") }
    }

    private fun showAllHabits() {
        habitContainer.removeAllViews()
        completedContainer.removeAllViews()
        for (habit in habitList) {
            if (habit.completed) addCompletedHabitCard(habit)
            else addHabitCard(habit)
        }
    }

    private fun filterHabits(time: String) {
        habitContainer.removeAllViews()
        completedContainer.removeAllViews()
        for (habit in habitList) {
            if (habit.timeOfDay == time) {
                if (habit.completed) addCompletedHabitCard(habit)
                else addHabitCard(habit)
            }
        }
    }

    // =========================
    // Habit Card UI Logic
    // =========================
    private fun addHabitCard(habit: Habit) {
        val card = CardView(this).apply {
            setCardBackgroundColor(habit.color)
            radius = 16f
            cardElevation = 6f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 16, 0, 0) }
        }

        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val topRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val iconView = TextView(this).apply {
            text = habit.icon
            textSize = 28f
        }

        val nameView = TextView(this).apply {
            text = "${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setPadding(16, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val checkbox = CheckBox(this)
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                habit.completed = true
                habit.progress = 100
                habitContainer.removeView(card)
                addCompletedHabitCard(habit)
            }
        }

        topRow.addView(iconView)
        topRow.addView(nameView)
        topRow.addView(checkbox)

        // Progress bar and percentage
        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = habit.progress
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                24
            ).apply { topMargin = 12 }
        }

        val progressText = TextView(this).apply {
            text = "${habit.progress}%"
            textSize = 14f
            setPadding(0, 4, 0, 0)
        }

        linearLayout.addView(topRow)
        linearLayout.addView(progressBar)
        linearLayout.addView(progressText)

        card.addView(linearLayout)
        habitContainer.addView(card)

        // Increment progress on click
        card.setOnClickListener {
            if (habit.progress < 100) {
                habit.progress += 10
                if (habit.progress > 100) habit.progress = 100
                progressBar.progress = habit.progress
                progressText.text = "${habit.progress}%"
                if (habit.progress == 100) {
                    habit.completed = true
                    habitContainer.removeView(card)
                    addCompletedHabitCard(habit)
                }
            }
        }
    }

    private fun addCompletedHabitCard(habit: Habit) {
        val card = CardView(this).apply {
            setCardBackgroundColor(Color.LTGRAY)
            radius = 16f
            cardElevation = 6f
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, 16, 0, 0) }
        }

        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(24, 24, 24, 24)
            gravity = Gravity.CENTER_VERTICAL
        }

        val iconView = TextView(this).apply {
            text = habit.icon
            textSize = 28f
        }

        val nameView = TextView(this).apply {
            text = "${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setPadding(16, 0, 0, 0)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }

        val progressText = TextView(this).apply {
            text = "100%"
            textSize = 14f
            setPadding(16, 0, 0, 0)
        }

        linearLayout.addView(iconView)
        linearLayout.addView(nameView)
        linearLayout.addView(progressText)
        card.addView(linearLayout)
        completedContainer.addView(card)
    }
}
