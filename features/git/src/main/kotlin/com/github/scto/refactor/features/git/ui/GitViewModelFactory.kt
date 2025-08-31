package com.github.scto.refactor.features.git.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.scto.refactor.features.git.di.AppContainer

class GitViewModelFactory(
    private val appContainer: AppContainer,
    private val context: android.content.Context,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GitViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GitViewModel(
                context = context,
                repositoryDao = appContainer.gitDatabase.repositoryDao(),
                userSettingsStore = appContainer.userSettingsStore,
                cryptoManager = appContainer.cryptoManager, // HINZUGEFÜGT
            )
                as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
