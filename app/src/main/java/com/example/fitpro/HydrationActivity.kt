package com.example.fitpro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.appbar.MaterialToolbar

class HydrationActivity : AppCompatActivity() {

    private lateinit var tvNextHydration: TextView
    private lateinit var tvNextAmount: TextView
    private lateinit var rvHydrationSchedule: RecyclerView
    private lateinit var waterChart: BarChart
    private lateinit var tvTotalWater: TextView

    private lateinit var etTime: EditText
    private lateinit var etAmount: EditText
    private lateinit var btnAddHydration: Button

    private lateinit var adapter: HydrationAdapter
    private val hydrationList = mutableListOf(
        HydrationItem("8:30 AM", 100),
        HydrationItem("9:30 AM", 100),
        HydrationItem("10:30 AM", 250),
        HydrationItem("1:30 PM", 700),
        HydrationItem("3:30 PM", 700),
        HydrationItem("6:15 PM", 250)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydration)

        // Handle back button in toolbar
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Initialize views
        tvNextHydration = findViewById(R.id.tvNextHydration)
        tvNextAmount = findViewById(R.id.tvNextAmount)
        rvHydrationSchedule = findViewById(R.id.rvHydrationSchedule)
        waterChart = findViewById(R.id.waterChart)
        tvTotalWater = findViewById(R.id.tvTotalWater)
        etTime = findViewById(R.id.etTime)
        etAmount = findViewById(R.id.etAmount)
        btnAddHydration = findViewById(R.id.btnAddHydration)

        // Set up RecyclerView
        adapter = HydrationAdapter(hydrationList) { position, isChecked ->
            hydrationList[position].completed = isChecked
            updateChart()
            updateNextHydrationText()
        }

        rvHydrationSchedule.layoutManager = LinearLayoutManager(this)
        rvHydrationSchedule.adapter = adapter

        updateChart()
        updateNextHydrationText()

        // Handle Add Button Click
        btnAddHydration.setOnClickListener {
            val time = etTime.text.toString().trim()
            val amountText = etAmount.text.toString().trim()

            if (time.isNotEmpty() && amountText.isNotEmpty()) {
                val amount = amountText.toIntOrNull()
                if (amount != null && amount > 0) {
                    hydrationList.add(HydrationItem(time, amount))
                    adapter.notifyItemInserted(hydrationList.size - 1)
                    updateChart()
                    updateNextHydrationText()
                    etTime.text.clear()
                    etAmount.text.clear()
                }
            }
        }
    }

    private fun updateChart() {
        val entries = mutableListOf<BarEntry>()
        hydrationList.forEachIndexed { index, item ->
            val yValue = if (item.completed) item.amount.toFloat() else 0f
            entries.add(BarEntry(index.toFloat(), yValue))
        }

        val dataSet = BarDataSet(entries, "Water Intake (ml)").apply {
            colors = hydrationList.map {
                if (it.completed) resources.getColor(R.color.purple_500, null)
                else resources.getColor(R.color.gray, null)
            }
            valueTextSize = 12f
            valueTextColor = resources.getColor(android.R.color.black, null)
            setDrawValues(true)
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.6f
        }

        waterChart.apply {
            data = barData
            description.isEnabled = false

            xAxis.apply {
                granularity = 1f
                setDrawGridLines(false)
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                textColor = resources.getColor(android.R.color.black, null)
                textSize = 12f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val idx = value.toInt()
                        return if (idx in hydrationList.indices) hydrationList[idx].time else ""
                    }
                }
            }

            axisLeft.apply {
                axisMinimum = 0f
                textColor = resources.getColor(android.R.color.black, null)
                setDrawGridLines(true)
            }

            axisRight.isEnabled = false

            legend.isEnabled = true
            animateY(1200)
            setFitBars(true)
            invalidate()
        }

        val total = hydrationList.filter { it.completed }.sumOf { it.amount }
        tvTotalWater.text = "Total: $total ml"
    }

    private fun updateNextHydrationText() {
        val nextItem = hydrationList.firstOrNull { !it.completed }
        if (nextItem != null) {
            tvNextHydration.text = "Next hydration:"
            tvNextAmount.text = "${nextItem.amount} ml at ${nextItem.time}"
        } else {
            tvNextHydration.text = "All done! 🎉"
            tvNextAmount.text = ""
        }
    }
}
