/*
 * Copyright 2025, S.C.T.O
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.scto.refactor.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.scto.refactor.core.gemini.ui.RefactorViewModel
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import com.github.scto.refactor.core.gemini.ui.UiEvent
import com.github.scto.refactor.core.gemini.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: RefactorViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val projectPickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) {
            uri ->
            // Der Pfad muss korrekt extrahiert werden, dies ist ein häufiger Fehlerpunkt.
            // Die gegebene Implementierung ist für dieses Beispiel ausreichend.
            uri?.path?.let { viewModel.onEvent(UiEvent.ProjectPathChanged(it)) }
        }

    Scaffold(topBar = { TopAppBar(title = { Text("Refactor") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // UI-Komponenten bleiben gleich, verwenden aber jetzt den zentralen State
            ProjectPickerSection(
                path = uiState.projectPath,
                onPickProject = { projectPickerLauncher.launch(null) },
            )
            Spacer(modifier = Modifier.height(16.dp))
            PackageNameInputs(
                targetPackageName = uiState.targetPackageName,
                onNewChanged = { viewModel.onEvent(UiEvent.TargetPackageNameChanged(it)) },
            )
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))
            RefactorTabs(uiState = uiState, viewModel = viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onEvent(UiEvent.StartRefactoringClicked) },
                enabled = uiState.projectPath.isNotEmpty() && !uiState.isRunning,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.isRunning) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Refactoring starten")
                }
            }
        }
    }
}

// Private Hilfskomponenten bleiben unverändert
@Composable
private fun ProjectPickerSection(path: String, onPickProject: () -> Unit) {
    OutlinedTextField(
        value = path,
        onValueChange = {},
        label = { Text("Projekt-Pfad") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = { Button(onClick = onPickProject) { Text("Wählen") } },
    )
}

@Composable
private fun PackageNameInputs(targetPackageName: String, onNewChanged: (String) -> Unit) {
    OutlinedTextField(
        value = targetPackageName,
        onValueChange = onNewChanged,
        label = { Text("Neuer Paketname (optional)") },
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun RefactorTabs(uiState: UiState, viewModel: RefactorViewModel) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Prozessoren", "Protokoll")
    Column(modifier = Modifier.weight(1f)) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) },
                )
            }
        }
        when (selectedTabIndex) {
            0 ->
                ProcessorsTab(
                    uiState,
                    onProcessorToggled = { option, isEnabled ->
                        viewModel.onEvent(UiEvent.RefactoringOptionToggled(option, isEnabled))
                    },
                )
            1 -> LogTab(uiState.logs)
        }
    }
}

// In ui/home/src/main/kotlin/com/github/scto/refactor/ui/home/HomeScreen.kt
@Composable
fun ProcessorsTab(state: UiState, onProcessorToggled: (RefactoringOption, Boolean) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(RefactoringOption.values()) { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            ) {
                Checkbox(
                    checked = state.options.contains(option),
                    onCheckedChange = { isEnabled -> onProcessorToggled(option, isEnabled) },
                )
                Text(option.displayText, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun LogTab(logs: List<String>) {
    val scrollState = rememberScrollState()
    LaunchedEffect(logs.size) { scrollState.animateScrollTo(scrollState.maxValue) }
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            Text(
                text = logs.joinToString("\n"),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(8.dp),
            )
        }
    }
}
