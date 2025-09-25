package com.example.fitpro

data class HydrationItem(
    val time: String,
    val amount: Int,
    var completed: Boolean = false
)
