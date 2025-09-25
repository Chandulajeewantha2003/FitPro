package com.example.fitpro

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HydrationAdapter(
    private val items: List<HydrationItem>,
    private val onClick: (HydrationItem) -> Unit
) : RecyclerView.Adapter<HydrationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hydration, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.tvTime.text = item.time
        holder.tvAmount.text = "${item.amount} ml"
        holder.itemView.alpha = if (item.completed) 0.5f else 1.0f

        holder.itemView.setOnClickListener {
            onClick(item)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = items.size
}
