
package com.example.watchdogslauncher.ui.bitchat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BitChatRepository {

    fun getConversations(): Flow<List<Conversation>> {
        return flowOf(
            listOf(
                Conversation("1", "Alice", "Hey, how are you?", "10:00 AM"),
                Conversation("2", "Bob", "See you at the meeting.", "Yesterday")
            )
        )
    }

    fun getMessages(conversationId: String): Flow<List<Message>> {
        return flowOf(
            listOf(
                Message("1", "1", "Hi!", "10:00 AM", false),
                Message("2", "1", "Hey, how are you?", "10:01 AM", true)
            )
        )
    }
}
