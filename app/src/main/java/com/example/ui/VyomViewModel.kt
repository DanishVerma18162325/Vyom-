package com.example.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.ModelRouter
import com.example.data.local.VyomDatabase
import com.example.data.local.VyomRepository
import com.example.data.model.AutomationRuleEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.DeviceCapabilityState
import com.example.data.model.MemoryEntity
import com.example.data.model.MessageEntity
import com.example.data.model.VyomAssistantState
import com.example.service.CapabilityDetector
import com.example.service.DeviceActionExecutor
import com.example.service.VoiceAssistantManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ActiveScreenMode {
    HOME,
    LIVE_VOICE,
    CIRCLE_SEARCH,
    VISION,
    STUDY,
    AUTOMATIONS,
    MEMORY,
    PRIVACY
}

class VyomViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VyomRepository
    private val modelRouter = ModelRouter()
    private val deviceActions = DeviceActionExecutor(application)
    private val capabilityDetector = CapabilityDetector(application)

    private val _assistantState = MutableStateFlow(VyomAssistantState.IDLE)
    val assistantState: StateFlow<VyomAssistantState> = _assistantState.asStateFlow()

    private val _screenMode = MutableStateFlow(ActiveScreenMode.HOME)
    val screenMode: StateFlow<ActiveScreenMode> = _screenMode.asStateFlow()

    private val _currentConversationId = MutableStateFlow(UUID.randomUUID().toString())
    val currentConversationId: StateFlow<String> = _currentConversationId.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _lastAiResponse = MutableStateFlow("")
    val lastAiResponse: StateFlow<String> = _lastAiResponse.asStateFlow()

    private val _deviceCapabilities = MutableStateFlow(DeviceCapabilityState())
    val deviceCapabilities: StateFlow<DeviceCapabilityState> = _deviceCapabilities.asStateFlow()

    val conversations: StateFlow<List<ConversationEntity>>
    val currentMessages: MutableStateFlow<List<MessageEntity>> = MutableStateFlow(emptyList())
    val memories: StateFlow<List<MemoryEntity>>
    val automations: StateFlow<List<AutomationRuleEntity>>

    val voiceManager: VoiceAssistantManager

    init {
        val db = VyomDatabase.getInstance(application)
        repository = VyomRepository(db.vyomDao())

        conversations = repository.activeConversations.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        memories = repository.allMemories.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        automations = repository.allAutomations.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        // Initialize voice manager
        voiceManager = VoiceAssistantManager(application) { recognizedText ->
            if (recognizedText.isNotBlank()) {
                sendMessage(recognizedText, isFromVoice = true)
            }
        }

        // Probing device capabilities
        _deviceCapabilities.value = capabilityDetector.detectCapabilities()

        // Observe messages of current conversation
        observeCurrentMessages()
    }

    private fun observeCurrentMessages() {
        viewModelScope.launch {
            repository.getMessages(_currentConversationId.value).collect { msgs ->
                currentMessages.value = msgs
            }
        }
    }

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setScreenMode(mode: ActiveScreenMode) {
        _screenMode.value = mode
    }

    fun selectConversation(conversationId: String) {
        _currentConversationId.value = conversationId
        observeCurrentMessages()
        _screenMode.value = ActiveScreenMode.HOME
    }

    fun startNewConversation() {
        val newId = UUID.randomUUID().toString()
        _currentConversationId.value = newId
        observeCurrentMessages()
        _screenMode.value = ActiveScreenMode.HOME
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            repository.deleteConversation(id)
            if (_currentConversationId.value == id) {
                startNewConversation()
            }
        }
    }

    fun sendMessage(
        query: String,
        bitmap: Bitmap? = null,
        isFromVoice: Boolean = false
    ) {
        if (query.isBlank() && bitmap == null) return

        _inputText.value = ""
        val convId = _currentConversationId.value

        viewModelScope.launch {
            // 1. Save user message
            repository.saveMessage(
                conversationId = convId,
                isUser = true,
                text = query
            )

            // 2. Set thinking state
            _assistantState.value = if (query.lowercase().contains("create image")) {
                VyomAssistantState.GENERATING_IMAGE
            } else if (query.lowercase().contains("search")) {
                VyomAssistantState.SEARCHING
            } else {
                VyomAssistantState.THINKING
            }

            // 3. Process via Model Router (Cloud Gemini or Local Provider)
            val result = modelRouter.routeQuery(
                prompt = query,
                bitmap = bitmap
            )

            _lastAiResponse.value = result.responseText

            // 4. Save response to database
            repository.saveMessage(
                conversationId = convId,
                isUser = false,
                text = result.responseText,
                toolName = result.toolName,
                toolResult = result.toolPayload,
                sources = result.sources
            )

            // 5. Update state & speak if initiated via voice
            _assistantState.value = VyomAssistantState.IDLE

            if (isFromVoice) {
                _assistantState.value = VyomAssistantState.SPEAKING
                val isHindi = query.contains("hindi", ignoreCase = true) ||
                        result.responseText.contains("नमस्ते") ||
                        result.responseText.contains("व्योम")
                voiceManager.speak(result.responseText, isHindi = isHindi)
            }
        }
    }

    fun executeDeviceAction(toolName: String, payload: String) {
        when (toolName) {
            "setAlarm" -> deviceActions.executeAlarm(payload)
            "setTimer" -> deviceActions.executeTimer(5)
            "callContact" -> deviceActions.executeCallContact(payload)
            "webSearch" -> deviceActions.executeWebSearch(payload)
            "openApp" -> deviceActions.executeOpenApp(payload)
            else -> {}
        }
    }

    fun speakText(text: String) {
        _assistantState.value = VyomAssistantState.SPEAKING
        val isHindi = text.contains("नमस्ते") || text.contains("व्योम")
        voiceManager.speak(text, isHindi = isHindi)
    }

    fun stopSpeaking() {
        voiceManager.stopSpeaking()
        _assistantState.value = VyomAssistantState.IDLE
    }

    fun startListening() {
        _assistantState.value = VyomAssistantState.LISTENING
        voiceManager.startListening()
    }

    fun stopListening() {
        voiceManager.stopListening()
        _assistantState.value = VyomAssistantState.IDLE
    }

    fun shareText(text: String) {
        deviceActions.shareText(text)
    }

    fun saveToMemory(content: String, category: String = "Preferences") {
        viewModelScope.launch {
            repository.saveMemory(category = category, content = content)
        }
    }

    fun deleteMemory(id: Long) {
        viewModelScope.launch {
            repository.deleteMemory(id)
        }
    }

    fun clearAllMemories() {
        viewModelScope.launch {
            repository.clearMemories()
        }
    }

    fun toggleAutomation(rule: AutomationRuleEntity) {
        viewModelScope.launch {
            repository.updateAutomation(rule)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceManager.release()
    }
}
