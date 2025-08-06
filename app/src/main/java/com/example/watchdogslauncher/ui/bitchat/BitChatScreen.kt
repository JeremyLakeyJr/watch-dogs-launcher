
package com.example.watchdogslauncher.ui.bitchat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun BitChatScreen() {
    val context = LocalContext.current
    val nsdHelper = NsdHelper(context)

    DisposableEffect(Unit) {
        nsdHelper.registerService(8888) // You can choose any available port
        nsdHelper.discoverServices()

        onDispose {
            nsdHelper.unregisterService()
            nsdHelper.stopDiscovery()
        }
    }

    val repository = BitChatRepository()
    val conversations by repository.getConversations().collectAsState(initial = emptyList())

    LazyColumn {
        items(conversations) {
            ConversationItem(it)
        }
    }
}

@Composable
fun ConversationItem(conversation: Conversation) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = conversation.name)
        Text(text = conversation.lastMessage)
    }
}
