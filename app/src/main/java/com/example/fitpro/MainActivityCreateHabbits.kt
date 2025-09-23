package com.example.fitpro

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        setupHabitTypeToggle()
        setupEmojiSelection()
        setupColorSelection()
        setupCalendar()
        setupTimeOfDayToggle()
        setupTimePicker()
        setupSaveButton()
    }

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


    private fun setupEmojiSelection() {
        val emojiViews = emojiContainer.findViewsWithType(TextView::class.java)
        emojiViews.forEach { emojiView ->
            emojiView.setOnClickListener {
                selectedIcon = emojiView.text.toString()
                // Reset all emojis (remove border)
                emojiViews.forEach { it.background = null }
                // Add border to selected emoji
                emojiView.setBackgroundResource(R.drawable.emoji_selected_border)
            }
        }
    }

    private fun setupColorSelection() {
        for (i in 0 until colorGrid.childCount) {
            val colorView = colorGrid.getChildAt(i)
            colorView.setOnClickListener {
                for (j in 0 until colorGrid.childCount) {
                    colorGrid.getChildAt(j).alpha = 1.0f
                }
                colorView.alpha = 0.5f
                selectedColor = colorView.backgroundTintList?.defaultColor ?: Color.GRAY
            }
        }
    }

    private fun setupCalendar() {
        val today = Calendar.getInstance()
        calendarView.minDate = today.timeInMillis // prevent past selection
        calendarView.date = today.timeInMillis // default to today

        selectedDay = today.get(Calendar.DAY_OF_MONTH)
        selectedMonth = today.get(Calendar.MONTH) + 1
        selectedYear = today.get(Calendar.YEAR)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDay = dayOfMonth
            selectedMonth = month + 1
            selectedYear = year
        }
    }

    private fun setupTimeOfDayToggle() {
        toggleTimeOfDay.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                selectedTimeOfDay = when (checkedId) {
                    R.id.btnMorning -> "Morning"
                    R.id.btnAfternoon -> "Afternoon"
                    R.id.btnEvening -> "Evening"
                    else -> null
                }
            }
        }
    }

    private fun setupTimePicker() {
        btnSelectTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = if (selectedHour != -1) selectedHour else calendar.get(Calendar.HOUR_OF_DAY)
            val minute = if (selectedMinute != -1) selectedMinute else calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minuteOfHour ->
                selectedHour = hourOfDay
                selectedMinute = minuteOfHour

                // Update button text
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                btnSelectTime.text = formattedTime

                // Save selectedTimeOfDay (don't reset morning/afternoon/evening)
                selectedTimeOfDay = formattedTime
            }, hour, minute, false)
            timePickerDialog.show()
        }
    }

    private fun setupSaveButton() {
        btnSaveHabit.setOnClickListener {
            val name = etHabitName.text.toString()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a habit name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            MainHome.habitList.add(
                MainHome.Habit(
                    name,
                    selectedIcon ?: "🎯",
                    selectedColor ?: Color.LTGRAY,
                    "$selectedDay/$selectedMonth/$selectedYear",
                    selectedTimeOfDay ?: ""
                )
            )
            finish()
        }
    }

    // Recursive extension to find all TextViews inside layout
    private fun <T : android.view.View> android.view.View.findViewsWithType(type: Class<T>): List<T> {
        val result = mutableListOf<T>()
        if (type.isInstance(this)) {
            result.add(this as T)
        }
        if (this is android.view.ViewGroup) {
            for (i in 0 until childCount) {
                result.addAll(getChildAt(i).findViewsWithType(type))
            }
        }
        return result
    }
}
