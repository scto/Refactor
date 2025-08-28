package com.github.scto.refactor.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(viewModel: SettingsViewModel, onNavigateBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Erscheinungsbild") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Abschnitt für die Theme-Auswahl
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ThemeSetting.values().forEach { theme ->
                ThemeOptionRow(
                    text = theme.name.lowercase().replaceFirstChar { it.titlecase() },
                    selected = uiState.theme == theme,
                    onClick = { viewModel.handleEvent(SettingsUiEvent.OnThemeChanged(theme)) }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Abschnitt für Dynamic Color mit einem Switch
            ListItem(
                headlineContent = { Text("Dynamische Farben") },
                supportingContent = { Text("Farben aus dem Hintergrundbild verwenden (nur Android 12+)") },
                trailingContent = {
                    Switch(
                        checked = uiState.useDynamicColor,
                        onCheckedChange = { useDynamicColor ->
                            viewModel.handleEvent(SettingsUiEvent.OnDynamicColorChanged(useDynamicColor))
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ThemeOptionRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
