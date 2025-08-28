package com.github.scto.refactor.features.git.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.scto.refactor.features.git.di.AppDataContainer
import com.github.scto.refactor.features.git.ui.GitContract.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GitScreen() {
    val context = LocalContext.current
    val appContainer = remember { AppDataContainer(context.applicationContext) }
    val viewModel: GitViewModel = viewModel(factory = GitViewModelFactory(appContainer, context))
    
    val uiState by viewModel.uiState.collectAsState()
    val contextForToast = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is GitEffect.ShowToast -> {
                    Toast.makeText(contextForToast, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    GitScreenContent(
        uiState = uiState,
        onEvent = viewModel::setEvent
    )
}

@Composable
fun GitScreenContent(
    uiState: GitUiState,
    onEvent: (GitEvent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is GitUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is GitUiState.Error -> {
                 Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is GitUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()), // Ermöglicht Scrollen bei kleinen Bildschirmen
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Git Client", style = MaterialTheme.typography.headlineMedium)

                    // Git Repository Felder
                    OutlinedTextField(value = uiState.repoUrl, onValueChange = { onEvent(GitEvent.OnRepoUrlChange(it)) }, label = { Text("Repository URL") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = uiState.localPath, onValueChange = { onEvent(GitEvent.OnLocalPathChange(it)) }, label = { Text("Lokaler Pfad") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = uiState.branchName, onValueChange = { onEvent(GitEvent.OnBranchNameChange(it)) }, label = { Text("Branch") }, modifier = Modifier.fillMaxWidth())

                    // Git Aktionen
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onEvent(GitEvent.OnCloneClick) }, modifier = Modifier.weight(1f)) { Text("Clone") }
                        Button(onClick = { onEvent(GitEvent.OnPullClick) }, modifier = Modifier.weight(1f)) { Text("Pull") }
                    }
                    
                    Text("Status: ${uiState.statusMessage}", style = MaterialTheme.typography.bodyLarge)

                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    
                    Text("Benutzereinstellungen", style = MaterialTheme.typography.headlineSmall)
                    
                    // User Settings Felder
                    // KORRIGIERT: Fehlenden onValueChange-Handler hinzugefügt
                    OutlinedTextField(value = uiState.email, onValueChange = { onEvent(GitEvent.OnEmailChange(it)) }, label = { Text("E-Mail für Commits") }, modifier = Modifier.fillMaxWidth())
                    
                    OutlinedTextField(
                        value = uiState.credentialsUsername,
                        onValueChange = { onEvent(GitEvent.OnCredentialsUsernameChange(it)) },
                        label = { Text("Credentials Username") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.credentialsToken,
                        onValueChange = { onEvent(GitEvent.OnCredentialsTokenChange(it)) },
                        label = { Text("Credentials Token / Passwort") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation() // Verdeckt die Eingabe
                    )
                    
                    // KORRIGIERT: Event-Name angepasst (OnSaveSettingsClick -> SaveSettings)
                    Button(onClick = { onEvent(GitEvent.SaveSettings) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Einstellungen speichern")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GitScreenPreview() {
    GitScreenContent(
        uiState = GitUiState.Success(localPath = "/data/data/com.app/files/git_repo"),
        onEvent = {}
    )
}
