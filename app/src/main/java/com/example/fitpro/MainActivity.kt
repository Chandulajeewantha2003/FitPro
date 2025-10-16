package com.example.fitpro

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // your logo screen XML

        // Delay 5 seconds then move to MainActivity2 using lifecycleScope to avoid leaks
        lifecycleScope.launch {
            delay(5000)
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            finish()
        }
    }
}
