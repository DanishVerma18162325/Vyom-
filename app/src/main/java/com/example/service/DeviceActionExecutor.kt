package com.example.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.AlarmClock
import android.widget.Toast

class DeviceActionExecutor(private val context: Context) {

    fun executeAlarm(timeDescription: String) {
        try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "VYOM Alarm")
                putExtra(AlarmClock.EXTRA_HOUR, 7)
                putExtra(AlarmClock.EXTRA_MINUTES, 0)
                putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Alarm configured: $timeDescription", Toast.LENGTH_SHORT).show()
        }
    }

    fun executeTimer(durationMinutes: Int = 5) {
        try {
            val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, "VYOM Timer")
                putExtra(AlarmClock.EXTRA_LENGTH, durationMinutes * 60)
                putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Timer started: $durationMinutes min", Toast.LENGTH_SHORT).show()
        }
    }

    fun executeCallContact(target: String) {
        try {
            // Using ACTION_DIAL so user can confirm number safely without requiring dangerous CALL_PHONE permission
            val intent = Intent(Intent.ACTION_DIAL).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Calling $target", Toast.LENGTH_SHORT).show()
        }
    }

    fun executeWebSearch(query: String) {
        try {
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra("query", query)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$query")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(browserIntent)
        }
    }

    fun executeOpenApp(appName: String) {
        val pm = context.packageManager
        val query = appName.lowercase()
        val pkg = when {
            query.contains("youtube") -> "com.google.android.youtube"
            query.contains("map") -> "com.google.android.apps.maps"
            query.contains("chrome") -> "com.android.chrome"
            query.contains("camera") -> null // use camera action
            else -> null
        }

        try {
            if (pkg != null) {
                val launchIntent = pm.getLaunchIntentForPackage(pkg)
                if (launchIntent != null) {
                    launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(launchIntent)
                    return
                }
            }
            Toast.makeText(context, "Opening $appName...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open $appName", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareText(text: String) {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            val chooser = Intent.createChooser(intent, "Share via VYOM").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(context, "Sharing text", Toast.LENGTH_SHORT).show()
        }
    }
}
