
package com.example.watchdogslauncher.ui.terminal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Terminal() {
    var command by remember { mutableStateOf("") }
    var output by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = command,
            onValueChange = { command = it },
            label = { Text("Enter command") }
        )
        Button(onClick = {
            if (command == "help") {
                output = output + "Available commands: help, clear"
            } else if (command == "clear") {
                output = listOf()
            } else {
                try {
                    val process = Runtime.getRuntime().exec(command)
                    val reader = BufferedReader(InputStreamReader(process.inputStream))
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        output = output + line!!
                    }
                } catch (e: Exception) {
                    output = output + e.message!!
                }
            }
            command = ""
        }) {
            Text(text = "Execute")
        }
        LazyColumn {
            items(output) { line ->
                Text(text = line)
            }
        }
    }
}
