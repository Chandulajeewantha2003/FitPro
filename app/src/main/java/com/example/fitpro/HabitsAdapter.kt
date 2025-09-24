package com.example.fitpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HabitsAdapter(
    private val habits: List<Habit>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    private val onProgressClick: (Int) -> Unit
) : RecyclerView.Adapter<HabitsAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvHabitName)
        val tvPercent: TextView = itemView.findViewById(R.id.tvProgressPercent)
        val progress: ProgressBar = itemView.findViewById(R.id.progressHabit)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.tvName.text = habit.name
        holder.progress.progress = habit.progress
        holder.tvPercent.text = "${habit.progress}%"

        holder.btnEdit.setOnClickListener { onEdit(position) }
        holder.btnDelete.setOnClickListener { onDelete(position) }

        // Increment progress when the item is clicked
        holder.itemView.setOnClickListener { onProgressClick(position) }
    }

    override fun getItemCount(): Int = habits.size
}
