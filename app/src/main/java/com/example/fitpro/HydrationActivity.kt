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

    private val dailyProgress = mutableListOf<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hydration)

        // Initialize views
        tvNextHydration = findViewById(R.id.tvNextHydration)
        tvNextAmount = findViewById(R.id.tvNextAmount)
        rvHydrationSchedule = findViewById(R.id.rvHydrationSchedule)
        waterChart = findViewById(R.id.waterChart)
        tvTotalWater = findViewById(R.id.tvTotalWater)
        etTime = findViewById(R.id.etTime)
        etAmount = findViewById(R.id.etAmount)
        btnAddHydration = findViewById(R.id.btnAddHydration)

        // RecyclerView adapter
        adapter = HydrationAdapter(hydrationList) { item ->
            if (!item.completed) {
                item.completed = true
                dailyProgress.add(item.amount.toFloat())
                updateChart()
                tvNextHydration.text = "Completed ✅"
            }
        }

        rvHydrationSchedule.layoutManager = LinearLayoutManager(this)
        rvHydrationSchedule.adapter = adapter

        // Show first next drink
        tvNextHydration.text = "Ready to hydrate in: 47 min"
        tvNextAmount.text = "${hydrationList[0].amount} ml"

        // Draw initial empty chart
        updateChart()

        // Add custom hydration entry
        btnAddHydration.setOnClickListener {
            val time = etTime.text.toString()
            val amountText = etAmount.text.toString()

            if (time.isNotEmpty() && amountText.isNotEmpty()) {
                val amount = amountText.toInt()
                val newItem = HydrationItem(time, amount)
                hydrationList.add(newItem)
                adapter.notifyItemInserted(hydrationList.size - 1)

                // Clear inputs
                etTime.text.clear()
                etAmount.text.clear()
            }
        }
    }

    private fun updateChart() {
        val entries = mutableListOf<BarEntry>()
        dailyProgress.forEachIndexed { index, amount ->
            entries.add(BarEntry(index.toFloat(), amount))
        }

        val dataSet = BarDataSet(entries, "Water Intake (ml)")
        dataSet.color = resources.getColor(R.color.AccentColor3, null)

        val barData = BarData(dataSet)
        barData.barWidth = 0.5f

        waterChart.data = barData
        waterChart.description.isEnabled = false
        waterChart.animateY(1000)
        waterChart.invalidate()

        // Update total water consumed
        val total = dailyProgress.sum()
        tvTotalWater.text = "Total: ${total.toInt()} ml"
    }
}
