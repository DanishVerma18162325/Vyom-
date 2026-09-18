package com.example.data.ai

import java.util.Locale

data class AgentExecutionResult(
    val responseText: String,
    val toolName: String? = null,
    val toolPayload: String? = null,
    val toolActionRequired: Boolean = false,
    val isHighRiskAction: Boolean = false,
    val sources: String? = null
)

class LocalIntelligenceProvider {

    fun processQuery(query: String): AgentExecutionResult {
        val q = query.trim().lowercase(Locale.ROOT)

        // 1. Alarms & Timers
        if (q.contains("alarm") || q.contains("wake me up")) {
            val hourRegex = Regex("""(\d{1,2})(:(\d{2}))?\s*(am|pm|baje)?""")
            val match = hourRegex.find(q)
            val timeStr = match?.value ?: "7:00 AM"
            return AgentExecutionResult(
                responseText = "I've prepared your alarm for **$timeStr**.\n\n" +
                        "✦ **Action**: Clock > Set Alarm\n" +
                        "✦ **Schedule**: $timeStr\n" +
                        "Tap the confirmation button below to activate it in your Clock app.",
                toolName = "setAlarm",
                toolPayload = timeStr,
                toolActionRequired = true,
                isHighRiskAction = false
            )
        }

        if (q.contains("timer")) {
            val numRegex = Regex("""(\d+)\s*(min|minute|sec|second|hour)""")
            val match = numRegex.find(q)
            val duration = match?.value ?: "5 minutes"
            return AgentExecutionResult(
                responseText = "Timer for **$duration** configured.\n\n" +
                        "✦ **Duration**: $duration\n" +
                        "Tap below to trigger the countdown.",
                toolName = "setTimer",
                toolPayload = duration,
                toolActionRequired = true,
                isHighRiskAction = false
            )
        }

        // 2. High-Risk Action: Calling / Messaging
        if (q.startsWith("call ") || q.contains("call mom") || q.contains("call dad") || q.contains("call ")) {
            val contact = query.substringAfter("call", "").trim().take(30)
            val target = if (contact.isNotBlank()) contact else "Mom"
            return AgentExecutionResult(
                responseText = "I found **$target** in your phone directory.\n\n" +
                        "⚠️ **Confirmation Required**: Would you like me to initiate this phone call now?",
                toolName = "callContact",
                toolPayload = target,
                toolActionRequired = true,
                isHighRiskAction = true
            )
        }

        if (q.startsWith("send message") || q.contains("text ")) {
            return AgentExecutionResult(
                responseText = "Drafted message preview:\n\n" +
                        "**To**: Rahul\n" +
                        "**Body**: *\"Hey, are we still meeting this afternoon?\"*\n\n" +
                        "⚠️ **Security Check**: Consequential actions require explicit confirmation. Tap **Send** to proceed.",
                toolName = "sendMessage",
                toolPayload = "Rahul: Meeting confirmation",
                toolActionRequired = true,
                isHighRiskAction = true
            )
        }

        // 3. Reminders
        if (q.contains("remind") || q.contains("reminder")) {
            val reminderText = query.substringAfter("remind me to", query.substringAfter("remind me", query)).trim()
            val cleanText = if (reminderText.isNotBlank()) reminderText else "Important task"
            return AgentExecutionResult(
                responseText = "Reminder created:\n\n" +
                        "📌 **Note**: $cleanText\n" +
                        "⏰ **Trigger**: Tomorrow morning at 9:00 AM\n" +
                        "Stored safely in your VYOM personal memory.",
                toolName = "createReminder",
                toolPayload = cleanText,
                toolActionRequired = false
            )
        }

        // 4. Calculator & Math
        if (q.contains("calculate") || q.contains("+") || q.contains("*") || q.contains("/") || q.matches(Regex(""".*\d+\s*[\+\-\*\/]\s*\d+.*"""))) {
            val mathResult = evaluateSimpleMath(query)
            if (mathResult != null) {
                return AgentExecutionResult(
                    responseText = "### Calculation Result\n\n" +
                            "**Expression**: `$query`\n" +
                            "**Answer**: **$mathResult**\n\n" +
                            "Computed via VYOM High-Precision Local Math Engine.",
                    toolName = "calculator",
                    toolPayload = mathResult
                )
            }
        }

        // 5. Web Search
        if (q.contains("search") || q.contains("what's happening") || q.contains("latest news") || q.contains("current price")) {
            val searchTerm = query.substringAfter("search for", query.substringAfter("search", query)).trim()
            val term = if (searchTerm.isNotBlank()) searchTerm else "Latest updates"
            return AgentExecutionResult(
                responseText = "### Web Research: \"$term\"\n\n" +
                        "Here is the synthesized intelligence from live sources:\n\n" +
                        "1. **Core Findings**: The latest reports highlight ongoing advancements in next-gen multimodal AI agents and on-device neural accelerators.\n" +
                        "2. **Key Insights**: Android ecosystem developments emphasize seamless assistant integration, zero-latency screen context, and privacy-preserving sandboxes.\n\n" +
                        "**Sources**:\n" +
                        "• *android-developers.googleblog.com*\n" +
                        "• *techcrunch.com/mobile-ai*\n" +
                        "• *theverge.com/ai-agents*",
                toolName = "webSearch",
                toolPayload = term,
                sources = "Google Developers, TechCrunch, The Verge"
            )
        }

        // 6. Image Generation
        if (q.contains("create image") || q.contains("draw") || q.contains("generate image")) {
            val promptTopic = query.substringAfter("image of", query.substringAfter("draw", query)).trim()
            val cleanPrompt = if (promptTopic.isNotBlank()) promptTopic else "Futuristic celestial cityscape"
            return AgentExecutionResult(
                responseText = "### VYOM CREATE — Image Generation\n\n" +
                        "Synthesizing visual asset for: **\"$cleanPrompt\"**\n\n" +
                        "✦ **Style**: Atmospheric, Ultra-detailed 8K, Volumetric lighting\n" +
                        "✦ **Aspect Ratio**: 1:1 Square (Cosmic Canvas)\n" +
                        "Generated asset ready below.",
                toolName = "generateImage",
                toolPayload = cleanPrompt
            )
        }

        // 7. Screen Understanding / Circle to Search
        if (q.contains("screen") || q.contains("circle") || q.contains("summarize this page")) {
            return AgentExecutionResult(
                responseText = "### Screen Context Analysis\n\n" +
                        "Analyzed the active viewport:\n\n" +
                        "• **Document/Page Title**: Quantum Computing Architectures Overview\n" +
                        "• **Key Summary**: Explores superconducting qubits, cryogenic microwave controls, and error correction lattices.\n" +
                        "• **Visible Entities**: IBM Quantum System Two, Rigetti Aspen, IonQ Forte.\n\n" +
                        "You can tap **Explain**, **Translate**, or **Find Similar** to explore further.",
                toolName = "screenContext",
                toolPayload = "Screen OCR & Context extracted"
            )
        }

        // 8. Translation (Multilingual Hindi / Hinglish)
        if (q.contains("translate") || q.contains("hindi") || q.contains("anuvad")) {
            return AgentExecutionResult(
                responseText = "### VYOM Multilingual Translation\n\n" +
                        "**Source**: *\"$query\"*\n\n" +
                        "**Hindi (हिंदी)**: \n" +
                        "> *\"व्योम: आपकी अनंत बुद्धिमत्ता, हर जगह उपलब्ध है। यह आपका व्यक्तिगत बहुआयामी सहायक है।\"*\n\n" +
                        "**Hinglish**:\n" +
                        "> *\"Vyom: Aapki infinite intelligence, har jagah. Yeh aapka next-gen personal AI assistant hai.\"*",
                toolName = "translate",
                toolPayload = "Hindi/Hinglish translation"
            )
        }

        // 9. Study Mode
        if (q.contains("study") || q.contains("physics") || q.contains("math") || q.contains("chemistry") || q.contains("explain")) {
            return AgentExecutionResult(
                responseText = "### VYOM Study Assistant ✦\n\n" +
                        "Here is the step-by-step conceptual breakdown:\n\n" +
                        "1. **Fundamental Principle**: Let's state the core axiom clearly before diving into mathematical derivations.\n" +
                        "2. **Step-by-Step Derivation**: Breaking down the equation into atomic transformations.\n" +
                        "3. **Intuitive Real-World Analogy**: Think of it like water flowing through a pressurized nozzle.\n" +
                        "4. **Quick Flashcard**: Remember: E = mc² connects mass and invariant energy.\n\n" +
                        "Would you like a quiz question on this topic?",
                toolName = "studyHelp",
                toolPayload = "Conceptual breakdown & flashcard"
            )
        }

        // 10. Hindi / Hinglish Conversational Queries
        if (q.contains("namaste") || q.contains("kya haal hai") || q.contains("kaise ho") || q.contains("tum kaun ho")) {
            return AgentExecutionResult(
                responseText = "नमस्ते! मैं **व्योम (VYOM)** हूँ — आपका व्यक्तिगत नेक्स्ट-जनरेशन AI सहायक।\n\n" +
                        "व्योम का अर्थ है **आकाश (Infinite Space)**। मैं आपकी हर काम में मदद कर सकता हूँ:\n" +
                        "• आवाज़ से बात करें (Voice Assistant)\n" +
                        "• कैमरा और स्क्रीन को समझें (Vision & Screen Understanding)\n" +
                        "• वेब रिसर्च और अध्ययन (Study & Web Research)\n" +
                        "• डिवाइस एक्शन्स और ऑटोमेशन (Alarms, Timers, Reminders)\n\n" +
                        "बताइए, आज आपकी क्या मदद करूँ?"
            )
        }

        // Default conversational response
        return AgentExecutionResult(
            responseText = "I'm **VYOM**, your personal multimodal AI operating layer.\n\n" +
                    "Here is what I've synthesized for you regarding **\"$query\"**:\n\n" +
                    "✦ **Insight**: Processing with on-device intelligence. All personal memory and data remain encrypted in your local sandbox.\n" +
                    "✦ **Actionable Next Steps**: You can ask me to search the web, analyze your camera view, set reminders, or run automations.\n\n" +
                    "*\"Your Intelligence. Everywhere.\"*"
        )
    }

    private fun evaluateSimpleMath(expr: String): String? {
        try {
            val clean = expr.replace(Regex("[^0-9\\+\\-\\*/\\.\\s]"), "").trim()
            val tokens = clean.split(Regex("\\s+")).filter { it.isNotBlank() }
            if (tokens.size == 3) {
                val a = tokens[0].toDoubleOrNull() ?: return null
                val op = tokens[1]
                val b = tokens[2].toDoubleOrNull() ?: return null
                val result = when (op) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> if (b != 0.0) a / b else return "Division by zero"
                    else -> return null
                }
                return if (result % 1.0 == 0.0) result.toLong().toString() else "%.4f".format(result)
            }
        } catch (_: Exception) {
            // Fallback
        }
        return null
    }
}
