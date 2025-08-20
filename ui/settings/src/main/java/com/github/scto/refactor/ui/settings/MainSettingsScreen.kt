package com.github.scto.refactor.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Einstellungen") }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SettingsItem(
                title = "Erscheinungsbild",
                subtitle = "Passe das Farbschema der App an",
                onClick = { navController.navigate(SettingsScreen.Theme.route) }
            )
            Divider()
            SettingsItem(
                title = "Über Refactor",
                subtitle = "Version, Lizenzen und mehr",
                onClick = { navController.navigate(SettingsScreen.About.route) }
            )
            Divider()
        }
    }
}

@Composable
fun SettingsItem(title: String, subtitle: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
}