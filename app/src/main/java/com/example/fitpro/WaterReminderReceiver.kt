package com.example.fitpro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val builder = NotificationCompat.Builder(context, "water_channel")
            .setSmallIcon(R.drawable.ic_drop)
            .setContentTitle("Hydration Reminder 💧")
            .setContentText("Time to drink some water!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}
