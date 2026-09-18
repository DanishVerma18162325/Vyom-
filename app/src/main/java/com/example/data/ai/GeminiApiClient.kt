package com.example.data.ai

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class GeminiApiClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateContent(
        prompt: String,
        bitmap: Bitmap? = null,
        systemInstruction: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isNullOrBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("Gemini API key is not configured. Falling back to On-Device VYOM Intelligence.")
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        try {
            val rootJson = JSONObject()

            // System instruction
            val sysInstructionText = systemInstruction ?: (
                "You are VYOM (meaning Akash / Infinite Sky), the ultimate next-generation personal AI assistant. " +
                "Your tagline is 'Your Intelligence. Everywhere.' You are intelligent, futuristic, calm, concise, " +
                "and helpful. You support English, Hindi, and Hinglish. Format your responses with clean Markdown, " +
                "bullet points, bold highlights, and code blocks where helpful. Never output robotic jargon."
            )

            val sysInstructionObj = JSONObject()
            val sysPartsArr = JSONArray()
            sysPartsArr.put(JSONObject().put("text", sysInstructionText))
            sysInstructionObj.put("parts", sysPartsArr)
            rootJson.put("systemInstruction", sysInstructionObj)

            // Contents
            val contentsArr = JSONArray()
            val contentObj = JSONObject()
            val partsArr = JSONArray()

            // Add text prompt
            partsArr.put(JSONObject().put("text", prompt))

            // Add image if provided
            if (bitmap != null) {
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                val base64Data = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                val inlineDataObj = JSONObject()
                inlineDataObj.put("mimeType", "image/jpeg")
                inlineDataObj.put("data", base64Data)

                val imagePartObj = JSONObject()
                imagePartObj.put("inlineData", inlineDataObj)
                partsArr.put(imagePartObj)
            }

            contentObj.put("parts", partsArr)
            contentsArr.put(contentObj)
            rootJson.put("contents", contentsArr)

            // Generation config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            genConfig.put("topP", 0.95)
            rootJson.put("generationConfig", genConfig)

            val requestBody = rootJson.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("Gemini API HTTP ${response.code}: $responseBody")
                )
            }

            val respJson = JSONObject(responseBody)
            val candidates = respJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textBuilder = StringBuilder()

            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.optJSONObject(i)
                    val t = part?.optString("text") ?: ""
                    textBuilder.append(t)
                }
            }

            val resultText = textBuilder.toString().trim()
            if (resultText.isNotEmpty()) {
                Result.success(resultText)
            } else {
                Result.failure(Exception("Empty response received from Gemini."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
