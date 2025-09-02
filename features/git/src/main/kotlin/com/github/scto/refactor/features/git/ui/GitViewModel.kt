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
package com.github.scto.refactor.features.git.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.github.scto.refactor.features.git.GitClient
import com.github.scto.refactor.features.git.JGitClient
import com.github.scto.refactor.features.git.UserSettings
import com.github.scto.refactor.features.git.crypto.CryptoManager
import com.github.scto.refactor.features.git.data.local.db.RepositoryDao
import com.github.scto.refactor.features.git.data.local.db.RepositoryEntity
import com.github.scto.refactor.features.git.ui.GitContract.GitEffect
import com.github.scto.refactor.features.git.ui.GitContract.GitEvent
import com.github.scto.refactor.features.git.ui.GitContract.GitUiState

import com.google.protobuf.ByteString
import java.io.File
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

class GitViewModel(
    context: Context,
    private val repositoryDao: RepositoryDao,
    private val userSettingsStore: DataStore<UserSettings>,
    private val cryptoManager: CryptoManager,
    private val gitClient: GitClient = JGitClient(),
) : ViewModel() {

    private val _uiState: MutableStateFlow<GitUiState> =
        MutableStateFlow(
            GitUiState.Success(localPath = File(context.filesDir, "git_repo").absolutePath)
        )
    val uiState: StateFlow<GitUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<GitEffect>()
    val effect: SharedFlow<GitEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            userSettingsStore.data.collect { settings ->
                _uiState.update {
                    (it as? GitUiState.Success)?.copy(
                        email = settings.email,
                        credentialsUsername = settings.username,
                        credentialsToken =
                            if (settings.credentials.isEmpty) ""
                            else cryptoManager.decrypt(settings.credentials.toByteArray()),
                    ) ?: it
                }
            }
        }
    }

    fun setEvent(event: GitEvent) {
        viewModelScope.launch {
            when (event) {
                is GitEvent.OnRepoUrlChange ->
                    updateSuccessState { it.copy(repoUrl = event.newUrl) }
                is GitEvent.OnLocalPathChange ->
                    updateSuccessState { it.copy(localPath = event.newPath) }
                is GitEvent.OnBranchNameChange ->
                    updateSuccessState { it.copy(branchName = event.newName) }
                is GitEvent.OnEmailChange -> updateSuccessState { it.copy(email = event.newEmail) }
                is GitEvent.OnCredentialsUsernameChange ->
                    updateSuccessState { it.copy(credentialsUsername = event.username) }
                is GitEvent.OnCredentialsTokenChange ->
                    updateSuccessState { it.copy(credentialsToken = event.token) }
                GitEvent.OnCloneClick -> cloneRepository()
                GitEvent.OnPullClick -> pullChanges()
                GitEvent.SaveSettings -> saveUserSettings()
            }
        }
    }

    private suspend fun cloneRepository() {
        val currentState = _uiState.value
        if (currentState !is GitUiState.Success) return

        _uiState.value = GitUiState.Loading
        try {
            val directory = File(currentState.localPath)
            if (directory.exists()) {
                directory.deleteRecursively()
            }
            gitClient.clone(currentState.repoUrl, directory, branch = currentState.branchName)

            val repositoryEntity =
                RepositoryEntity(
                    repoUrl = currentState.repoUrl,
                    localPath = currentState.localPath,
                    branchName = currentState.branchName,
                )
            repositoryDao.insertRepository(repositoryEntity)

            _uiState.value = currentState.copy(statusMessage = "Clone erfolgreich!")
            _effect.emit(GitEffect.ShowToast("Repository erfolgreich geklont und gespeichert."))
        } catch (e: Exception) {
            _uiState.value = GitUiState.Error("Fehler beim Clonen: ${e.message}")
        }
    }

    private suspend fun pullChanges() {
        val currentState = _uiState.value
        if (currentState !is GitUiState.Success) return

        _uiState.value = GitUiState.Loading
        try {
            val credentialsProvider =
                if (
                    currentState.credentialsUsername.isNotBlank() &&
                        currentState.credentialsToken.isNotBlank()
                ) {
                    UsernamePasswordCredentialsProvider(
                        currentState.credentialsUsername,
                        currentState.credentialsToken,
                    )
                } else {
                    null
                }

            gitClient.pull(File(currentState.localPath), credentialsProvider)
            _uiState.value = currentState.copy(statusMessage = "Pull erfolgreich!")
        } catch (e: Exception) {
            _uiState.value = GitUiState.Error("Fehler beim Pullen: ${e.message}")
        }
    }

    private suspend fun saveUserSettings() {
        val currentState = _uiState.value
        if (currentState !is GitUiState.Success) return

        val encryptedCredentials = cryptoManager.encrypt(currentState.credentialsToken)

        userSettingsStore.updateData { settings ->
            settings
                .toBuilder()
                .setUsername(currentState.credentialsUsername)
                .setEmail(currentState.email)
                .setCredentials(ByteString.copyFrom(encryptedCredentials))
                .build()
        }
        _effect.emit(GitEffect.ShowToast("Einstellungen sicher gespeichert!"))
    }

    private fun updateSuccessState(update: (GitUiState.Success) -> GitUiState.Success) {
        _uiState.update { (it as? GitUiState.Success)?.let(update) ?: it }
    }
}
