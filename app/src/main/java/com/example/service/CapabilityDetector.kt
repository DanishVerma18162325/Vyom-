package com.example.service

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.speech.SpeechRecognizer
import com.example.data.model.DeviceCapabilityState

class CapabilityDetector(private val context: Context) {

    fun detectCapabilities(): DeviceCapabilityState {
        val pm = context.packageManager

        val hasSpeech = SpeechRecognizer.isRecognitionAvailable(context)
        val hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

        // Connectivity check
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = cm?.activeNetwork
        val caps = cm?.getNetworkCapabilities(network)
        val hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

        // Android 14+ AICore / on-device foundation model check simulation
        val hasOnDeviceAi = Build.VERSION.SDK_INT >= 34

        return DeviceCapabilityState(
            hasSpeechRecognizer = hasSpeech,
            hasTextToSpeech = true,
            hasCamera = hasCamera,
            hasInternet = hasInternet,
            hasOnDeviceAi = hasOnDeviceAi,
            assistantRoleAvailable = true,
            supportedLanguages = listOf("English (Global)", "हिंदी (Hindi)", "Hinglish (Conversational)")
        )
    }
}
