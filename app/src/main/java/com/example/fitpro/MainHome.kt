package com.example.fitpro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        habitContainer = findViewById(R.id.habitContainer)
        completedContainer = findViewById(R.id.completedContainer)

        buttonAll = findViewById(R.id.buttonAll)
        buttonMorning = findViewById(R.id.buttonMorning)
        buttonAfternoon = findViewById(R.id.buttonAfternoon)
        buttonEvening = findViewById(R.id.buttonEvening)

        setupTimeOfDayButtons()

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home
        bottomNav.setOnItemSelectedListener { item ->
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

        // Notification bell icon click
        val bellIcon = findViewById<ImageView>(R.id.bellIcon)
        bellIcon.setOnClickListener {
            val intent = Intent(this, HydrationActivity::class.java)
            intent.putExtra("show_next", true)
            startActivity(intent)
        }

        if (habitList.isEmpty()) {
            habitList.addAll(
                listOf(
                    Habit("Drink 2L of Water", "\uD83D\uDCA7", getColor(R.color.colorTertiary),
                        "2025-10-01", "Morning", "08:00", false, 30),
                    Habit("30-Minute Walk", "\uD83C\uDFC3", Color.parseColor("#81C784"),
                        "2025-10-01", "Afternoon", "14:00", false, 50),
                    Habit("Journal for 10 Minutes", "\uD83D\uDCDD", Color.parseColor("#FFAB91"),
                        "2025-10-01", "Evening", "21:00", false, 10)
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

    private fun highlightButton(selected: MaterialButton) {
        val buttons = listOf(buttonAll, buttonMorning, buttonAfternoon, buttonEvening)
        buttons.forEach { btn ->
            if (btn == selected) {
                btn.setBackgroundColor(getColor(R.color.colorPrimary))
                btn.setTextColor(getColor(R.color.colorOnPrimary))
            } else {
                btn.setBackgroundColor(getColor(R.color.colorSurface))
                btn.setTextColor(getColor(R.color.colorPrimary))
            }
        }
    }

    private fun filterHabits(timeOfDay: String) {
        habitContainer.removeAllViews()
        completedContainer.removeAllViews()

        val filtered = if (timeOfDay.equals("All", ignoreCase = true)) {
            habitList
        } else {
            habitList.filter { it.timeOfDay.equals(timeOfDay, ignoreCase = true) }
        }

        filtered.forEach { habit ->
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

        val ll = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val info = TextView(this).apply {
            text = "${habit.icon} ${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        ll.addView(info)

        val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            progress = habit.progress
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                24
            ).apply { topMargin = 16 }
        }
        ll.addView(progressBar)

        val progressText = TextView(this).apply {
            text = "Progress: ${habit.progress}%"
            textSize = 14f
            setTextColor(Color.DKGRAY)
            gravity = Gravity.END
        }
        ll.addView(progressText)

        val btnComplete = MaterialButton(this).apply {
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
        ll.addView(btnComplete)

        val btnInc = MaterialButton(this).apply {
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
        ll.addView(btnInc)

        card.addView(ll)
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

        val ll = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val info = TextView(this).apply {
            text = "${habit.icon} ${habit.name}\n${habit.date} | ${habit.timeOfDay} (${habit.exactTime})"
            textSize = 18f
            setTextColor(Color.DKGRAY)
        }
        ll.addView(info)

        val doneText = TextView(this).apply {
            text = "Completed!"
            textSize = 16f
            setTextColor(Color.GREEN)
            gravity = Gravity.END
        }
        ll.addView(doneText)

        card.addView(ll)
        completedContainer.addView(card)
    }
}
