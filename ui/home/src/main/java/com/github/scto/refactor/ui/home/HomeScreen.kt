package com.github.scto.refactor.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: RefactorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val projectPickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri ->
        uri?.let { viewModel.handleEvent(RefactorUiEvent.ProjectPathSelected(it)) }
    }
    Scaffold(topBar = { TopAppBar(title = { Text("Refactor") }) }) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProjectPickerSection(path = uiState.projectPath?.path) { projectPickerLauncher.launch(null) }
            Spacer(modifier = Modifier.height(16.dp))
            PackageNameInputs(
                uiState.oldPackageName,
                uiState.newPackageName,
                { viewModel.handleEvent(RefactorUiEvent.OldPackageNameChanged(it)) },
                { viewModel.handleEvent(RefactorUiEvent.NewPackageNameChanged(it)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            RefactorTabs(
                uiState,
                { viewModel.handleEvent(RefactorUiEvent.TabSelected(it)) },
                { viewModel.handleEvent(RefactorUiEvent.ToggleProcessor(it)) },
                { viewModel.handleEvent(it) },
                { viewModel.handleEvent(RefactorUiEvent.StartRefactoring) }
            )
        }
    }
}

@Composable
private fun ProjectPickerSection(path: String?, onPickProject: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = onPickProject) { Text("Select Project Path") }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Project Path:", style = MaterialTheme.typography.titleMedium)
        Text(path ?: "No project selected", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
    }
}

@Composable
private fun PackageNameInputs(old: String, new: String, onOld: (String) -> Unit, onNew: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(value = old, onValueChange = onOld, label = { Text("Old Package Name") }, modifier = Modifier.weight(1f))
        OutlinedTextField(value = new, onValueChange = onNew, label = { Text("New Package Name") }, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun RefactorTabs(
    state: RefactorUiState,
    onTab: (Int) -> Unit,
    onProc: (Processor) -> Unit,
    onSet: (RefactorUiEvent) -> Unit,
    onStart: () -> Unit
) {
    val tabs = listOf("Processors", "Settings", "Run")
    Column {
        TabRow(selectedTabIndex = state.selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(selected = state.selectedTabIndex == index, onClick = { onTab(index) }, text = { Text(title) })
            }
        }
        when (state.selectedTabIndex) {
            0 -> ProcessorsTab(state, onProc)
            1 -> SettingsTab(state, onSet)
            2 -> RunTab(onStart)
        }
    }
}

@Composable
fun ProcessorsTab(state: RefactorUiState, onProc: (Processor) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Processor.values().forEach { p ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.selectedProcessors[p] ?: false, onCheckedChange = { onProc(p) })
                Text(p.description, Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
fun SettingsTab(state: RefactorUiState, onSet: (RefactorUiEvent) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        SettingCheckBox("Show Log Messages", state.showLog) { onSet(RefactorUiEvent.ToggleShowLog) }
        SettingCheckBox("Show Suggestions", state.showSuggestions) { onSet(RefactorUiEvent.ToggleShowSuggestions) }
        SettingCheckBox("Show Final Result", state.showResult) { onSet(RefactorUiEvent.ToggleShowResult) }
    }
}

@Composable
fun SettingCheckBox(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked, onCheckedChange)
        Text(label, Modifier.padding(start = 8.dp))
    }
}

@Composable
fun RunTab(onStart: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onStart) { Text("Start Refactoring") }
    }
}
