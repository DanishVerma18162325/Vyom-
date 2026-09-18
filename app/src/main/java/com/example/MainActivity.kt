package com.example

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ScreenSearchDesktop
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.ActiveScreenMode
import com.example.ui.VyomViewModel
import com.example.ui.components.VyomAtmosphericBackground
import com.example.ui.components.VyomInputBar
import com.example.ui.components.VyomTopBar
import com.example.ui.screens.AutomationsScreen
import com.example.ui.screens.CircleSearchOverlay
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveVoiceScreen
import com.example.ui.screens.MemoryScreen
import com.example.ui.screens.PrivacyScreen
import com.example.ui.screens.SidebarDrawer
import com.example.ui.screens.StudyScreen
import com.example.ui.screens.VisionScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.VyomAtmosphereBlue
import com.example.ui.theme.VyomAtmospherePurple
import com.example.ui.theme.VyomGlassBorder
import com.example.ui.theme.VyomInkMuted
import com.example.ui.theme.VyomInkPrimary
import com.example.ui.theme.VyomPureWhite
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: VyomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Assistant Intent Handling: If triggered as Android Assistant (Long-press power or swipe corner)
        val action = intent?.action
        if (action == Intent.ACTION_ASSIST || action == "android.intent.action.VOICE_COMMAND") {
            viewModel.setScreenMode(ActiveScreenMode.LIVE_VOICE)
        }

        setContent {
            MyApplicationTheme {
                VyomApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VyomApp(viewModel: VyomViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val assistantState by viewModel.assistantState.collectAsState()
    val screenMode by viewModel.screenMode.collectAsState()
    val currentConvId by viewModel.currentConversationId.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val currentMessages by viewModel.currentMessages.collectAsState()
    val memories by viewModel.memories.collectAsState()
    val automations by viewModel.automations.collectAsState()
    val lastAiResponse by viewModel.lastAiResponse.collectAsState()

    val isListening by viewModel.voiceManager.isListening.collectAsState()
    val isSpeaking by viewModel.voiceManager.isSpeaking.collectAsState()
    val audioRms by viewModel.voiceManager.audioRms.collectAsState()
    val spokenTranscript by viewModel.voiceManager.lastSpokenText.collectAsState()

    var showToolsSheet by remember { mutableStateOf(false) }

    // Audio Permission Request
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            Toast.makeText(context, "Microphone permission required for voice input", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera Permission Request
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.setScreenMode(ActiveScreenMode.VISION)
        } else {
            Toast.makeText(context, "Camera permission required for vision", Toast.LENGTH_SHORT).show()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = VyomPureWhite) {
                SidebarDrawer(
                    conversations = conversations,
                    currentConversationId = currentConvId,
                    onSelectConversation = { id ->
                        viewModel.selectConversation(id)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onNewChat = {
                        viewModel.startNewConversation()
                        coroutineScope.launch { drawerState.close() }
                    },
                    onDeleteConversation = { id ->
                        viewModel.deleteConversation(id)
                    },
                    onOpenLiveVoice = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.LIVE_VOICE)
                    },
                    onOpenVision = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.VISION)
                    },
                    onOpenStudy = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.STUDY)
                    },
                    onOpenAutomations = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.AUTOMATIONS)
                    },
                    onOpenMemory = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.MEMORY)
                    },
                    onOpenPrivacy = {
                        coroutineScope.launch { drawerState.close() }
                        viewModel.setScreenMode(ActiveScreenMode.PRIVACY)
                    }
                )
            }
        }
    ) {
        VyomAtmosphericBackground(
            state = assistantState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (screenMode) {
                ActiveScreenMode.LIVE_VOICE -> {
                    LiveVoiceScreen(
                        state = assistantState,
                        audioRms = audioRms,
                        isListening = isListening,
                        isSpeaking = isSpeaking,
                        spokenTranscript = spokenTranscript,
                        lastAiResponse = lastAiResponse,
                        onStartListening = {
                            val perm = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                            if (perm == PackageManager.PERMISSION_GRANTED) {
                                viewModel.startListening()
                            } else {
                                recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        onStopListening = { viewModel.stopListening() },
                        onStopSpeaking = { viewModel.stopSpeaking() },
                        onClose = {
                            viewModel.stopSpeaking()
                            viewModel.stopListening()
                            viewModel.setScreenMode(ActiveScreenMode.HOME)
                        }
                    )
                }

                ActiveScreenMode.VISION -> {
                    VisionScreen(
                        onAnalyzeScene = { question, bitmap ->
                            viewModel.setScreenMode(ActiveScreenMode.HOME)
                            viewModel.sendMessage(question, bitmap = bitmap)
                        },
                        onClose = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                    )
                }

                ActiveScreenMode.STUDY -> {
                    StudyScreen(
                        onLaunchStudyTopic = { prompt ->
                            viewModel.setScreenMode(ActiveScreenMode.HOME)
                            viewModel.sendMessage(prompt)
                        },
                        onBack = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                    )
                }

                ActiveScreenMode.AUTOMATIONS -> {
                    AutomationsScreen(
                        automations = automations,
                        onToggleAutomation = { viewModel.toggleAutomation(it) },
                        onRunBriefing = {
                            viewModel.setScreenMode(ActiveScreenMode.HOME)
                            viewModel.sendMessage("Give me my Good Morning with VYOM daily briefing (Weather, Calendar, Priorities, Top News)")
                        },
                        onBack = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                    )
                }

                ActiveScreenMode.MEMORY -> {
                    MemoryScreen(
                        memories = memories,
                        onAddMemory = { cat, content -> viewModel.saveToMemory(content, cat) },
                        onDeleteMemory = { viewModel.deleteMemory(it) },
                        onClearAll = { viewModel.clearAllMemories() },
                        onBack = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                    )
                }

                ActiveScreenMode.PRIVACY -> {
                    PrivacyScreen(
                        onBack = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                    )
                }

                ActiveScreenMode.HOME, ActiveScreenMode.CIRCLE_SEARCH -> {
                    Scaffold(
                        containerColor = Color.Transparent,
                        topBar = {
                            VyomTopBar(
                                state = assistantState,
                                activeModelName = "Gemini 3.5 & Local",
                                onMenuClick = {
                                    coroutineScope.launch { drawerState.open() }
                                },
                                onPrivacyClick = {
                                    viewModel.setScreenMode(ActiveScreenMode.PRIVACY)
                                },
                                onNewChatClick = {
                                    viewModel.startNewConversation()
                                },
                                modifier = Modifier.statusBarsPadding()
                            )
                        },
                        bottomBar = {
                            VyomInputBar(
                                text = inputText,
                                onTextChange = { viewModel.setInputText(it) },
                                onSend = { viewModel.sendMessage(it) },
                                onMicClick = {
                                    val perm = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                                    if (perm == PackageManager.PERMISSION_GRANTED) {
                                        viewModel.startListening()
                                    } else {
                                        recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                },
                                onCameraClick = {
                                    val perm = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                                    if (perm == PackageManager.PERMISSION_GRANTED) {
                                        viewModel.setScreenMode(ActiveScreenMode.VISION)
                                    } else {
                                        cameraLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                onAttachmentClick = { showToolsSheet = true },
                                onLiveVoiceClick = {
                                    viewModel.setScreenMode(ActiveScreenMode.LIVE_VOICE)
                                }
                            )
                        }
                    ) { innerPadding ->
                        HomeScreen(
                            state = assistantState,
                            messages = currentMessages,
                            onSuggestionClick = { prompt ->
                                if (prompt.contains("live multimodal voice")) {
                                    viewModel.setScreenMode(ActiveScreenMode.LIVE_VOICE)
                                } else if (prompt.contains("screen context")) {
                                    viewModel.setScreenMode(ActiveScreenMode.CIRCLE_SEARCH)
                                } else {
                                    viewModel.sendMessage(prompt)
                                }
                            },
                            onSpeak = { viewModel.speakText(it) },
                            onShare = { viewModel.shareText(it) },
                            onRegenerate = {
                                currentMessages.lastOrNull { it.isUser }?.text?.let {
                                    viewModel.sendMessage(it)
                                }
                            },
                            onSaveToMemory = { viewModel.saveToMemory(it) },
                            onExecuteAction = { toolName, payload ->
                                viewModel.executeDeviceAction(toolName, payload)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    // Circle to Search overlay if active
                    if (screenMode == ActiveScreenMode.CIRCLE_SEARCH) {
                        CircleSearchOverlay(
                            onActionSelected = { action, prompt ->
                                viewModel.sendMessage("[$action Screen Context]: $prompt")
                            },
                            onClose = { viewModel.setScreenMode(ActiveScreenMode.HOME) }
                        )
                    }
                }
            }
        }
    }

    // Tools & Attachment Sheet
    if (showToolsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showToolsSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VyomPureWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "✦ VYOM CAPABILITIES & TOOLS",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = VyomInkMuted,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                ToolSheetItem(
                    title = "Circle to Search (VYOM Circle)",
                    subtitle = "Draw circles over screen content to identify and search",
                    icon = Icons.Default.ScreenSearchDesktop,
                    iconTint = VyomAtmosphereBlue,
                    onClick = {
                        showToolsSheet = false
                        viewModel.setScreenMode(ActiveScreenMode.CIRCLE_SEARCH)
                    }
                )

                ToolSheetItem(
                    title = "Vision Camera",
                    subtitle = "Real-time camera object & text analysis",
                    icon = Icons.Default.CameraAlt,
                    iconTint = Color(0xFF06B6D4),
                    onClick = {
                        showToolsSheet = false
                        viewModel.setScreenMode(ActiveScreenMode.VISION)
                    }
                )

                ToolSheetItem(
                    title = "Study Assistant Mode",
                    subtitle = "Step-by-step homework help, flashcards & quizzes",
                    icon = Icons.Default.School,
                    iconTint = VyomAtmospherePurple,
                    onClick = {
                        showToolsSheet = false
                        viewModel.setScreenMode(ActiveScreenMode.STUDY)
                    }
                )

                ToolSheetItem(
                    title = "Good Morning Daily Briefing",
                    subtitle = "Audio summary of weather, calendar, and news",
                    icon = Icons.Default.Bolt,
                    iconTint = Color(0xFFF59E0B),
                    onClick = {
                        showToolsSheet = false
                        viewModel.sendMessage("Deliver my Good Morning with VYOM daily briefing.")
                    }
                )

                ToolSheetItem(
                    title = "Personal Memory Vault",
                    subtitle = "Manage your preferences, rules, and encrypted context",
                    icon = Icons.Default.Bookmark,
                    iconTint = Color(0xFF10B981),
                    onClick = {
                        showToolsSheet = false
                        viewModel.setScreenMode(ActiveScreenMode.MEMORY)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ToolSheetItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = VyomInkPrimary
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = VyomInkMuted
            )
        }
    }
}
