package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {

    private val selectedImages = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        val imgRunning = findViewById<ImageView>(R.id.imageView5)
        val imgWalking = findViewById<ImageView>(R.id.imageView6)
        val imgMeal = findViewById<ImageView>(R.id.imageView9)
        val imgCycling = findViewById<ImageView>(R.id.imageView10)
        val imgYoga = findViewById<ImageView>(R.id.imageView11)
        val imgHealth = findViewById<ImageView>(R.id.imageView12)

        val images = listOf(imgRunning, imgWalking, imgMeal, imgCycling, imgYoga, imgHealth)

        images.forEach { image ->
            image.setOnClickListener {
                val id = image.id
                if (selectedImages.contains(id)) {
                    // deselect
                    selectedImages.remove(id)
                    image.background = null
                } else {
                    // select
                    selectedImages.add(id)
                    image.setBackgroundResource(R.drawable.image_selected_border)
                }
            }
        }

        val btnNext = findViewById<Button>(R.id.button4)
        btnNext.setOnClickListener {
            if (selectedImages.isEmpty()) {
                // No selection
                android.widget.Toast.makeText(this, "Please select at least one option", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                // Go to MainActivity4
                val intent = Intent(this, MainActivity4::class.java)
                startActivity(intent)
            }
        }
    }
}
