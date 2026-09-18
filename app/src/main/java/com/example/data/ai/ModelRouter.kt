package com.example.data.ai

import android.graphics.Bitmap

class ModelRouter(
    private val geminiClient: GeminiApiClient = GeminiApiClient(),
    private val localProvider: LocalIntelligenceProvider = LocalIntelligenceProvider()
) {

    suspend fun routeQuery(
        prompt: String,
        bitmap: Bitmap? = null,
        forceLocal: Boolean = false
    ): AgentExecutionResult {
        // If user forced local or offline mode, or for device action queries
        val isDeviceAction = prompt.lowercase().let {
            it.startsWith("call ") || it.startsWith("alarm") || it.contains("timer") || it.startsWith("set alarm")
        }

        if (forceLocal || isDeviceAction) {
            return localProvider.processQuery(prompt)
        }

        // Try cloud Gemini 3.5 Flash first
        val cloudResult = geminiClient.generateContent(
            prompt = prompt,
            bitmap = bitmap
        )

        return if (cloudResult.isSuccess) {
            val responseText = cloudResult.getOrNull() ?: ""
            // Detect if this is an image generation request
            val isImg = prompt.lowercase().contains("create image") || prompt.lowercase().contains("generate image")
            AgentExecutionResult(
                responseText = responseText,
                toolName = if (isImg) "generateImage" else if (bitmap != null) "visionAnalysis" else null,
                toolPayload = if (isImg) prompt else null
            )
        } else {
            // Graceful fallback to Local Intelligence Engine
            val localResult = localProvider.processQuery(prompt)
            localResult.copy(
                responseText = localResult.responseText + "\n\n*(Processed via VYOM On-Device Intelligence Engine)*"
            )
        }
    }
}
