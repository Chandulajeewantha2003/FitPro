package com.example.fitpro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainHome : AppCompatActivity() {

    private lateinit var habitContainer: LinearLayout
    private lateinit var completedContainer: LinearLayout
    private lateinit var buttonAll: MaterialButton
    private lateinit var buttonMorning: MaterialButton
    private lateinit var buttonAfternoon: MaterialButton
    private lateinit var buttonEvening: MaterialButton

    companion object {
        val habitList = mutableListOf<Habit>()
    }

    data class Habit(
        val name: String,
        val icon: String,
        val color: Int,
        val date: String,
        val timeOfDay: String,
        val exactTime: String = "",
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

        // Buttons
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

        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, MainActivityCreateHabbits::class.java))
        }

        // Sample Habits
        if (habitList.isEmpty()) {
            habitList.addAll(
                listOf(
                    Habit(
                        name = "Drink 2L of Water",
                        icon = "\uD83D\uDCA7", // 💧
                        color = Color.parseColor("#4FC3F7"), // Light Blue
                        date = "2025-10-01",
                        timeOfDay = "Morning",
                        exactTime = "08:00",
                        completed = false,
                        progress = 30
                    ),
                    Habit(
                        name = "30-Minute Walk",
                        icon = "\uD83C\uDFC3", // 🏃
                        color = Color.parseColor("#81C784"), // Green
                        date = "2025-10-01",
                        timeOfDay = "Afternoon",
                        exactTime = "14:00",
                        completed = false,
                        progress = 50
                    ),
                    Habit(
                        name = "Journal for 10 Minutes",
                        icon = "\uD83D\uDCDD", // 📝
                        color = Color.parseColor("#FFAB91"), // Orange
                        date = "2025-10-01",
                        timeOfDay = "Evening",
                        exactTime = "21:00",
                        completed = false,
                        progress = 10
                    )
                )
            )
        }

        showAllHabits()
    }

    private fun setupTimeOfDayButtons() {
        val buttons = listOf(buttonAll, buttonMorning, buttonAfternoon, buttonEvening)
        buttons.forEach { btn ->
            btn.setOnClickListener {
                highlightButton(btn)
                filterHabits(btn.text.toString())
            }
        }
        highlightButton(buttonAll)
    }

    private fun highlightButton(selectedButton: MaterialButton) {
        val buttons = listOf(buttonAll, buttonMorning, buttonAfternoon, buttonEvening)
        buttons.forEach { btn ->
            if (btn == selectedButton) {
                btn.setBackgroundColor(Color.parseColor("#6A1B9A"))
                btn.setTextColor(Color.WHITE)
            } else {
                btn.setBackgroundColor(Color.parseColor("#FFFFFF"))
                btn.setTextColor(Color.parseColor("#6200EE"))
            }
        }
    }

    private fun filterHabits(timeOfDay: String) {
        habitContainer.removeAllViews()
        completedContainer.removeAllViews()

        val filteredHabits = if (timeOfDay.equals("All", ignoreCase = true)) {
            habitList
        } else {
            habitList.filter { it.timeOfDay.equals(timeOfDay, ignoreCase = true) }
        }

        filteredHabits.forEach { habit ->
            if (!habit.completed) {
                addHabitCard(habit)
            } else {
                addCompletedHabitCard(habit)
            }
        }
    }

    private fun showAllHabits() {
        filterHabits("All")
    }

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

        val habitInfo = TextView(this).apply {
            text = "${habit.icon} ${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        linearLayout.addView(habitInfo)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = habit.progress
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                24
            ).apply { topMargin = 16 }
        }
        linearLayout.addView(progressBar)

        val progressText = TextView(this).apply {
            text = "Progress: ${habit.progress}%"
            textSize = 14f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.END
        }
        linearLayout.addView(progressText)

        val btnCompleted = MaterialButton(this).apply {
            text = "Completed"
            setOnClickListener {
                habit.completed = true
                habit.progress = 100
                showAllHabits()
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 16 }
        }
        linearLayout.addView(btnCompleted)

        val btnIncreaseProgress = MaterialButton(this).apply {
            text = "Increase Progress"
            setOnClickListener {
                if (habit.progress < 100) {
                    habit.progress += 10
                    if (habit.progress > 100) habit.progress = 100
                    showAllHabits()
                }
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 8 }
        }
        linearLayout.addView(btnIncreaseProgress)

        card.addView(linearLayout)
        habitContainer.addView(card)
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
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val habitInfo = TextView(this).apply {
            text = "${habit.icon} ${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setTextColor(Color.DKGRAY)
        }
        linearLayout.addView(habitInfo)

        val completedText = TextView(this).apply {
            text = "Completed!"
            textSize = 16f
            setTextColor(Color.GREEN)
            gravity = Gravity.END
        }
        linearLayout.addView(completedText)

        card.addView(linearLayout)
        completedContainer.addView(card)
    }
}
