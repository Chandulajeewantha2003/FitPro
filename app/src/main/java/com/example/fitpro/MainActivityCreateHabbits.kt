package com.example.fitpro

import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.*

class MainActivityCreateHabbits : AppCompatActivity() {

    private lateinit var etHabitName: EditText
    private lateinit var toggleHabitType: MaterialButtonToggleGroup
    private lateinit var calendarView: CalendarView
    private lateinit var toggleTimeOfDay: MaterialButtonToggleGroup
    private lateinit var btnSaveHabit: Button
    private lateinit var emojiContainer: LinearLayout
    private lateinit var colorGrid: GridLayout
    private lateinit var btnSelectTime: Button

    private var selectedHabitType: String = "Regular"
    private var selectedIcon: String? = null
    private var selectedColor: Int? = null
    private var selectedDay: Int = -1
    private var selectedMonth: Int = -1
    private var selectedYear: Int = -1
    private var selectedTimeOfDay: String? = null
    private var selectedHour: Int = -1
    private var selectedMinute: Int = -1
    private var selectedExactTime: String? = null // HH:mm format

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_create_habbits)

        // Edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        etHabitName = findViewById(R.id.etHabitName)
        toggleHabitType = findViewById(R.id.toggleHabitType)
        calendarView = findViewById(R.id.calendarView)
        toggleTimeOfDay = findViewById(R.id.toggleTimeOfDay)
        btnSaveHabit = findViewById(R.id.btnSaveHabit)
        emojiContainer = findViewById(R.id.emojiContainer)
        colorGrid = findViewById(R.id.colorGrid)
        btnSelectTime = findViewById(R.id.btnSelectTime)

        // Setup all functions
        setupHabitTypeToggle()
        setupEmojiSelection()
        setupColorSelection()
        setupCalendar()
        setupTimeOfDayToggle()
        setupTimePicker()
        setupSaveButton()

        // Bottom navigation logic
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.nav_meal

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainHome::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.nav_meal -> true // Already here
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
    }

    // ------------------------------
    // Habit Type Toggle
    // ------------------------------
    private fun setupHabitTypeToggle() {
        toggleHabitType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedHabitType = when (checkedId) {
                    R.id.btnRegular -> "Regular"
                    R.id.btnOneTime -> "One-Time"
                    else -> "Regular"
                }
            }
        }
    }

        // ------------------------------
        // Emoji Selection
        // ------------------------------
        private fun setupEmojiSelection() {
        val emojiViews = emojiContainer.findViewsWithType(TextView::class.java)
        emojiViews.forEach { emojiView ->
            emojiView.setOnClickListener {
                selectedIcon = emojiView.text.toString()
                emojiViews.forEach { it.background = null } // reset border
                emojiView.setBackgroundResource(R.drawable.emoji_selected_border)
            }
        }
    }

    // ------------------------------
    // Color Selection
    // ------------------------------
    private fun setupColorSelection() {
        for (i in 0 until colorGrid.childCount) {
            val colorView = colorGrid.getChildAt(i)
            colorView.setOnClickListener {
                for (j in 0 until colorGrid.childCount) colorGrid.getChildAt(j).alpha = 1.0f
                colorView.alpha = 0.5f
                selectedColor = colorView.backgroundTintList?.defaultColor ?: Color.GRAY
            }
        }
    }

    // ------------------------------
    // Calendar
    // ------------------------------
    private fun setupCalendar() {
        val today = Calendar.getInstance()
        calendarView.minDate = today.timeInMillis
        calendarView.date = today.timeInMillis

        selectedDay = today.get(Calendar.DAY_OF_MONTH)
        selectedMonth = today.get(Calendar.MONTH) + 1
        selectedYear = today.get(Calendar.YEAR)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDay = dayOfMonth
            selectedMonth = month + 1
            selectedYear = year
        }
    }

    // ------------------------------
    // Time of Day Toggle
    // ------------------------------
    private fun setupTimeOfDayToggle() {
        toggleTimeOfDay.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedTimeOfDay = when (checkedId) {
                    R.id.btnMorning -> "Morning"
                    R.id.btnAfternoon -> "Afternoon"
                    R.id.btnEvening -> "Evening"
                    else -> null
                }
                // Update default time immediately
                setDefaultTimeForTimeOfDay()
            }
        }
    }

    // ------------------------------
    // Time Picker
    // ------------------------------
    private fun setupTimePicker() {
        btnSelectTime.setOnClickListener {
            val defaultHour = if (selectedHour != -1) selectedHour else getDefaultHour()
            val defaultMinute = if (selectedMinute != -1) selectedMinute else 0

            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minuteOfHour ->
                    selectedHour = hourOfDay
                    selectedMinute = minuteOfHour
                    selectedExactTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    btnSelectTime.text = selectedExactTime
                },
                defaultHour,
                defaultMinute,
                false
            )
            timePickerDialog.show()
        }
    }

    private fun getDefaultHour(): Int {
        return when (selectedTimeOfDay) {
            "Morning" -> 0       // 12 AM
            "Afternoon" -> 12    // 12 PM
            "Evening" -> 18      // 6 PM
            else -> 0
        }
    }

    private fun setDefaultTimeForTimeOfDay() {
        selectedHour = getDefaultHour()
        selectedMinute = 0
        selectedExactTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        btnSelectTime.text = selectedExactTime
    }

    // ------------------------------
    // Save Button
    // ------------------------------
    private fun setupSaveButton() {
        btnSaveHabit.setOnClickListener {
            val name = etHabitName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a habit name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ensure timeOfDay and exact time are selected
            val timeOfDay = selectedTimeOfDay ?: "Morning"
            val exactTime = selectedExactTime ?: when(timeOfDay) {
                "Morning" -> "12:00 AM"
                "Afternoon" -> "12:00 PM"
                "Evening" -> "06:00 PM"
                else -> "12:00 AM"
            }

            MainHome.habitList.add(
                MainHome.Habit(
                    name = name,
                    icon = selectedIcon ?: "🎯",
                    color = selectedColor ?: Color.LTGRAY,
                    date = "$selectedDay/$selectedMonth/$selectedYear",
                    timeOfDay = timeOfDay, //timeOfDay = "$timeOfDay ($exactTime)"
                    completed = false,
                    progress = 0
                )
            )

            Toast.makeText(this, "Habit saved! ($timeOfDay at $exactTime)", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // Recursive extension to find all TextViews inside a layout
    private fun <T : android.view.View> android.view.View.findViewsWithType(type: Class<T>): List<T> {
        val result = mutableListOf<T>()
        if (type.isInstance(this)) result.add(this as T)
        if (this is android.view.ViewGroup) {
            for (i in 0 until childCount) result.addAll(getChildAt(i).findViewsWithType(type))
        }
        return result
    }
}
