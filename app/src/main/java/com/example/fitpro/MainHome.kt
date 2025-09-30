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

        // Sample Data (optional)
        if (habitList.isEmpty()) {
            habitList.addAll(
                listOf(
                    Habit("Morning Run", "\uD83C\uDFC3", Color.parseColor("#FFA726"), "2025-10-01", "Morning", "06:30", false, 20),
                    Habit("Read Book", "\uD83D\uDCD6", Color.parseColor("#29B6F6"), "2025-10-01", "Afternoon", "13:00"),
                    Habit("Evening Yoga", "\uD83E\uDD38", Color.parseColor("#AB47BC"), "2025-10-01", "Evening", "19:00", false, 50)
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
        // Initially highlight "All"
        highlightButton(buttonAll)
    }

    private fun highlightButton(selectedButton: MaterialButton) {
        val buttons = listOf(buttonAll, buttonMorning, buttonAfternoon, buttonEvening)
        buttons.forEach { btn ->
            if (btn == selectedButton) {
                btn.setBackgroundColor(Color.parseColor("#6A1B9A")) // Purple
                btn.setTextColor(Color.WHITE)
            } else {
                btn.setBackgroundColor(Color.parseColor("#FFFFFF")) // White
                btn.setTextColor(Color.parseColor("#6200EE")) // primaryColor
            }
        }
    }

    private fun filterHabits(timeOfDay: String) {
        habitContainer.removeAllViews()
        completedContainer.removeAllViews()

        val filteredHabits = if (timeOfDay == "All") {
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

        // Habit name and info
        val habitInfo = TextView(this).apply {
            text = "${habit.icon} ${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        linearLayout.addView(habitInfo)

        // Progress bar
        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = habit.progress
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                24
            ).apply { topMargin = 16 }
        }
        linearLayout.addView(progressBar)

        // Text showing progress percentage
        val progressText = TextView(this).apply {
            text = "Progress: ${habit.progress}%"
            textSize = 14f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.END
        }
        linearLayout.addView(progressText)

        // Button to mark completed
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

        // Button to increase progress (for demo)
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
