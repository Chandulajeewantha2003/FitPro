package com.example.fitpro

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivityChoseIcons : AppCompatActivity() {

    // Store the currently selected view
    private var selectedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_chose_icons)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the emoji grid
        val emojiGrid = findViewById<GridLayout>(R.id.emojiGrid)

        // Loop through each child (emoji TextViews)
        for (i in 0 until emojiGrid.childCount) {
            val emojiView = emojiGrid.getChildAt(i) as TextView

            emojiView.setOnClickListener {
                // Remove highlight from previous selection
                selectedView?.setBackgroundResource(R.drawable.bg_emoji_default)

                // Highlight new selection
                emojiView.setBackgroundResource(R.drawable.bg_emoji_selected)

                // Save selected view
                selectedView = emojiView
            }
        }
    }
}
