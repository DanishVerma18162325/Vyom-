package com.example.data.local

import com.example.data.model.AutomationRuleEntity
import com.example.data.model.ConversationEntity
import com.example.data.model.MemoryEntity
import com.example.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class VyomRepository(private val dao: VyomDao) {

    val activeConversations: Flow<List<ConversationEntity>> = dao.getActiveConversations()
    val allMemories: Flow<List<MemoryEntity>> = dao.getAllMemories()
    val allAutomations: Flow<List<AutomationRuleEntity>> = dao.getAllAutomations()

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> {
        return dao.getMessagesForConversation(conversationId)
    }

    suspend fun createNewConversation(title: String = "New Conversation"): String {
        val id = UUID.randomUUID().toString()
        val conversation = ConversationEntity(
            id = id,
            title = title,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertConversation(conversation)
        return id
    }

    suspend fun saveMessage(
        conversationId: String,
        isUser: Boolean,
        text: String,
        imageUrl: String? = null,
        toolName: String? = null,
        toolResult: String? = null,
        sources: String? = null
    ): Long {
        val existingConv = dao.getConversationById(conversationId)
        if (existingConv == null) {
            val titleSnippet = if (isUser) {
                if (text.length > 28) text.take(28) + "…" else text
            } else "Conversation"
            dao.insertConversation(
                ConversationEntity(
                    id = conversationId,
                    title = titleSnippet,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            // Update title if it's currently "New Conversation" and user sends first message
            val updatedTitle = if (existingConv.title == "New Conversation" && isUser) {
                if (text.length > 28) text.take(28) + "…" else text
            } else {
                existingConv.title
            }
            dao.updateConversation(
                existingConv.copy(
                    title = updatedTitle,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }

        return dao.insertMessage(
            MessageEntity(
                conversationId = conversationId,
                isUser = isUser,
                text = text,
                imageUrl = imageUrl,
                toolName = toolName,
                toolResult = toolResult,
                sources = sources
            )
        )
    }

    suspend fun deleteConversation(id: String) {
        dao.deleteMessagesForConversation(id)
        dao.deleteConversation(id)
    }

    suspend fun renameConversation(id: String, newTitle: String) {
        val conv = dao.getConversationById(id) ?: return
        dao.updateConversation(conv.copy(title = newTitle, updatedAt = System.currentTimeMillis()))
    }

    suspend fun togglePinConversation(id: String) {
        val conv = dao.getConversationById(id) ?: return
        dao.updateConversation(conv.copy(isPinned = !conv.isPinned, updatedAt = System.currentTimeMillis()))
    }

    suspend fun archiveConversation(id: String) {
        val conv = dao.getConversationById(id) ?: return
        dao.updateConversation(conv.copy(isArchived = true, updatedAt = System.currentTimeMillis()))
    }

    suspend fun saveMemory(category: String, content: String): Long {
        return dao.insertMemory(
            MemoryEntity(category = category, content = content)
        )
    }

    suspend fun deleteMemory(id: Long) {
        dao.deleteMemory(id)
    }

    suspend fun clearMemories() {
        dao.clearAllMemories()
    }

    suspend fun saveAutomation(rule: AutomationRuleEntity): Long {
        return dao.insertAutomation(rule)
    }

    suspend fun updateAutomation(rule: AutomationRuleEntity) {
        dao.updateAutomation(rule)
    }

    suspend fun deleteAutomation(id: Long) {
        dao.deleteAutomation(id)
    }

    suspend fun seedDefaultsIfEmpty() {
        // Prepopulate default intelligent automations if empty
        // Non-blocking seed
    }
}
