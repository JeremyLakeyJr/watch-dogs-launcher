package com.example.watchdogslauncher.ui.terminal

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watchdogslauncher.model.AppInfo
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.*

sealed class TerminalOutput {
    data class Command(val text: String, val timestamp: Long) : TerminalOutput()
    data class Output(val text: String) : TerminalOutput()
    data class Error(val text: String) : TerminalOutput()
    data class Success(val text: String) : TerminalOutput()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTerminal(
    apps: List<AppInfo>,
    onDismiss: () -> Unit,
    enableAutocomplete: Boolean = true,
    historySize: Int = 50
) {
    val context = LocalContext.current
    var inputText by remember { mutableStateOf("") }
    val commandHistory = remember { mutableStateListOf<String>() }
    var historyIndex by remember { mutableStateOf(-1) }
    val outputList = remember { mutableStateListOf<TerminalOutput>() }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    // Autocomplete suggestions
    val suggestions = remember(inputText, apps) {
        if (enableAutocomplete && inputText.isNotEmpty()) {
            val commands = listOf("help", "clear", "list", "info", "search", "uninstall", "launch", "stats", "exit")
            val appNames = apps.map { it.label.toString() }
            (commands + appNames).filter { it.contains(inputText, ignoreCase = true) }.take(5)
        } else {
            emptyList()
        }
    }
    
    LaunchedEffect(outputList.size) {
        if (outputList.isNotEmpty()) {
            listState.animateScrollToItem(outputList.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Terminal header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "DEDSEC TERMINAL v2.0",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.primary)
                }
            }
            
            Divider(color = MaterialTheme.colorScheme.primary, thickness = 2.dp)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Output area
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState
            ) {
                items(outputList) { output ->
                    when (output) {
                        is TerminalOutput.Command -> {
                            val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                            Text(
                                "[${timeFormat.format(Date(output.timestamp))}] > ${output.text}",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                        is TerminalOutput.Output -> {
                            Text(
                                output.text,
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                        is TerminalOutput.Error -> {
                            Text(
                                "ERROR: ${output.text}",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = Color.Red
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                        is TerminalOutput.Success -> {
                            Text(
                                "✓ ${output.text}",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 14.sp,
                                    color = Color.Green
                                ),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            
            // Autocomplete suggestions
            if (suggestions.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        .padding(8.dp)
                ) {
                    Text(
                        "Suggestions:",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    )
                    suggestions.forEach { suggestion ->
                        Text(
                            suggestion,
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    inputText = suggestion
                                }
                                .padding(4.dp)
                        )
                    }
                }
            }
            
            // Input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "> ",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                TextField(
                    value = inputText,
                    onValueChange = { 
                        inputText = it
                        historyIndex = -1
                    },
                    modifier = Modifier.weight(1f),
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                    placeholder = { 
                        Text(
                            "Enter command...",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )
                    },
                    singleLine = true
                )
            }
            
            // Command buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (inputText.isNotEmpty()) {
                            executeCommand(
                                command = inputText,
                                context = context,
                                apps = apps,
                                outputList = outputList,
                                commandHistory = commandHistory,
                                historySize = historySize,
                                onDismiss = onDismiss
                            )
                            inputText = ""
                            historyIndex = -1
                        }
                    },
                    modifier = Modifier.weight(1f).padding(4.dp)
                ) {
                    Text("Execute", fontFamily = FontFamily.Monospace)
                }
                
                Button(
                    onClick = {
                        if (historyIndex < commandHistory.size - 1) {
                            historyIndex++
                            inputText = commandHistory[commandHistory.size - 1 - historyIndex]
                        }
                    },
                    modifier = Modifier.weight(1f).padding(4.dp),
                    enabled = commandHistory.isNotEmpty()
                ) {
                    Text("↑", fontFamily = FontFamily.Monospace)
                }
                
                Button(
                    onClick = {
                        if (historyIndex > 0) {
                            historyIndex--
                            inputText = commandHistory[commandHistory.size - 1 - historyIndex]
                        } else if (historyIndex == 0) {
                            historyIndex = -1
                            inputText = ""
                        }
                    },
                    modifier = Modifier.weight(1f).padding(4.dp),
                    enabled = historyIndex >= 0
                ) {
                    Text("↓", fontFamily = FontFamily.Monospace)
                }
            }
        }
    }
}

private fun executeCommand(
    command: String,
    context: Context,
    apps: List<AppInfo>,
    outputList: MutableList<TerminalOutput>,
    commandHistory: MutableList<String>,
    historySize: Int,
    onDismiss: () -> Unit
) {
    // Add to history
    commandHistory.add(command)
    if (commandHistory.size > historySize) {
        commandHistory.removeAt(0)
    }
    
    // Add command to output
    outputList.add(TerminalOutput.Command(command, System.currentTimeMillis()))
    
    val parts = command.trim().split(" ")
    val cmd = parts[0].lowercase()
    val args = parts.drop(1)
    
    when (cmd) {
        "help" -> {
            outputList.add(TerminalOutput.Output("Available commands:"))
            outputList.add(TerminalOutput.Output("  help - Show this help message"))
            outputList.add(TerminalOutput.Output("  clear - Clear terminal output"))
            outputList.add(TerminalOutput.Output("  list - List all installed apps"))
            outputList.add(TerminalOutput.Output("  search <query> - Search for apps"))
            outputList.add(TerminalOutput.Output("  launch <app> - Launch an application"))
            outputList.add(TerminalOutput.Output("  info <app> - Show app information"))
            outputList.add(TerminalOutput.Output("  uninstall <app> - Uninstall an application"))
            outputList.add(TerminalOutput.Output("  stats - Show system statistics"))
            outputList.add(TerminalOutput.Output("  exit - Close terminal"))
        }
        
        "clear" -> {
            outputList.clear()
            outputList.add(TerminalOutput.Success("Terminal cleared"))
        }
        
        "list" -> {
            outputList.add(TerminalOutput.Output("Installed applications (${apps.size}):"))
            apps.sortedBy { it.label.toString() }.forEach { app ->
                outputList.add(TerminalOutput.Output("  - ${app.label}"))
            }
        }
        
        "search" -> {
            if (args.isEmpty()) {
                outputList.add(TerminalOutput.Error("Usage: search <query>"))
            } else {
                val query = args.joinToString(" ")
                val results = apps.filter { it.label.contains(query, ignoreCase = true) }
                outputList.add(TerminalOutput.Output("Found ${results.size} app(s):"))
                results.forEach { app ->
                    outputList.add(TerminalOutput.Output("  - ${app.label}"))
                }
            }
        }
        
        "launch" -> {
            if (args.isEmpty()) {
                outputList.add(TerminalOutput.Error("Usage: launch <app_name>"))
            } else {
                val appName = args.joinToString(" ")
                val app = apps.find { it.label.toString().equals(appName, ignoreCase = true) }
                if (app != null) {
                    val intent = context.packageManager.getLaunchIntentForPackage(app.packageName.toString())
                    if (intent != null) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        outputList.add(TerminalOutput.Success("Launching ${app.label}..."))
                    } else {
                        outputList.add(TerminalOutput.Error("Cannot launch ${app.label}"))
                    }
                } else {
                    outputList.add(TerminalOutput.Error("App not found: $appName"))
                }
            }
        }
        
        "info" -> {
            if (args.isEmpty()) {
                outputList.add(TerminalOutput.Error("Usage: info <app_name>"))
            } else {
                val appName = args.joinToString(" ")
                val app = apps.find { it.label.toString().equals(appName, ignoreCase = true) }
                if (app != null) {
                    try {
                        val packageInfo = context.packageManager.getPackageInfo(
                            app.packageName.toString(),
                            PackageManager.GET_PERMISSIONS
                        )
                        outputList.add(TerminalOutput.Output("App: ${app.label}"))
                        outputList.add(TerminalOutput.Output("Package: ${app.packageName}"))
                        outputList.add(TerminalOutput.Output("Version: ${packageInfo.versionName}"))
                        val permissions = packageInfo.requestedPermissions?.size ?: 0
                        outputList.add(TerminalOutput.Output("Permissions: $permissions"))
                    } catch (e: Exception) {
                        outputList.add(TerminalOutput.Error("Failed to get app info: ${e.message}"))
                    }
                } else {
                    outputList.add(TerminalOutput.Error("App not found: $appName"))
                }
            }
        }
        
        "uninstall" -> {
            if (args.isEmpty()) {
                outputList.add(TerminalOutput.Error("Usage: uninstall <app_name>"))
            } else {
                val appName = args.joinToString(" ")
                val app = apps.find { it.label.toString().equals(appName, ignoreCase = true) }
                if (app != null) {
                    val intent = Intent(Intent.ACTION_DELETE).apply {
                        data = android.net.Uri.parse("package:${app.packageName}")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                    outputList.add(TerminalOutput.Success("Opening uninstall dialog for ${app.label}..."))
                } else {
                    outputList.add(TerminalOutput.Error("App not found: $appName"))
                }
            }
        }
        
        "stats" -> {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            val memoryInfo = android.app.ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memoryInfo)
            
            val totalMem = memoryInfo.totalMem / (1024 * 1024)
            val availMem = memoryInfo.availMem / (1024 * 1024)
            val usedMem = totalMem - availMem
            
            outputList.add(TerminalOutput.Output("=== System Statistics ==="))
            outputList.add(TerminalOutput.Output("Total RAM: ${totalMem}MB"))
            outputList.add(TerminalOutput.Output("Used RAM: ${usedMem}MB"))
            outputList.add(TerminalOutput.Output("Available RAM: ${availMem}MB"))
            outputList.add(TerminalOutput.Output("Installed Apps: ${apps.size}"))
        }
        
        "exit" -> {
            outputList.add(TerminalOutput.Success("Closing terminal..."))
            onDismiss()
        }
        
        else -> {
            // Try to launch app by name
            val app = apps.find { it.label.toString().equals(command, ignoreCase = true) }
            if (app != null) {
                val intent = context.packageManager.getLaunchIntentForPackage(app.packageName.toString())
                if (intent != null) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    outputList.add(TerminalOutput.Success("Launching ${app.label}..."))
                } else {
                    outputList.add(TerminalOutput.Error("Cannot launch ${app.label}"))
                }
            } else {
                outputList.add(TerminalOutput.Error("Unknown command: $command"))
                outputList.add(TerminalOutput.Output("Type 'help' for available commands"))
            }
        }
    }
}
