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
package com.github.scto.refactor.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource

import com.github.scto.refactor.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSettingsScreen(viewModel: SettingsViewModel, onNavigateBack: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
				title = { Text(stringResource(id = R.string.settings_about_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        // KORRIGIERT: Explizite Parameterbenennung zur Behebung der Mehrdeutigkeit
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = { Text(stringResource(id = R.string.action_back)) },
                        )
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
				Text(stringResource(id = R.string.app_name)),
				style = MaterialTheme.typography.headlineLarge),
				fontWeight = FontWeight.Bold,
				/*
                "Refactor",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
				*/
            )
            Spacer(modifier = Modifier.height(8.dp))
			Text(stringResource(id = R.string.settings_about_version), "${uiState.appVersion}", style = MaterialTheme.typography.bodyLarge),
            //Text("Version ${uiState.appVersion}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(24.dp))
			Text(stringResource(id = R.string.settings_about_developer), style = MaterialTheme.typography.bodyMedium),
            //Text("Entwickelt von [Dein Name/Team]", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/scto/refactor"))
                    context.startActivity(intent)
                }
            ) {
				Text(stringResource(id = R.string.settings_about_source_code_title))
                //Text("Source Code")
            }
        }
    }
}
