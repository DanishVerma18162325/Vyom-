package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val conversationId: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null,
    val toolName: String? = null,
    val toolResult: String? = null,
    val sources: String? = null // Comma-separated or JSON list of citations
)

@Entity(tableName = "conversations")
data class ConversationEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isPinned: Boolean = false,
    val isArchived: Boolean = false
)

@Entity(tableName = "memories")
data class MemoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String, // "Preferences", "Instructions", "Projects", "Frequent Tasks"
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "automations")
data class AutomationRuleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val triggerType: String,
    val actionType: String,
    val actionPayload: String,
    val isEnabled: Boolean = true
)

enum class VyomAssistantState {
    IDLE,
    THINKING,
    LISTENING,
    SPEAKING,
    SEARCHING,
    GENERATING_IMAGE,
    ERROR
}

data class DeviceCapabilityState(
    val hasSpeechRecognizer: Boolean = true,
    val hasTextToSpeech: Boolean = true,
    val hasCamera: Boolean = true,
    val hasInternet: Boolean = true,
    val hasOnDeviceAi: Boolean = false, // Gemini Nano / AICore detection
    val assistantRoleAvailable: Boolean = true,
    val supportedLanguages: List<String> = listOf("English", "हिंदी (Hindi)", "Hinglish")
)
