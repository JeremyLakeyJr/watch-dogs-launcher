
package com.example.watchdogslauncher.ui.bitchat

data class Conversation(
    val id: String,
    val name: String,
    val lastMessage: String,
    val timestamp: String
)

data class Message(
    val id: String,
    val conversationId: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean
)
