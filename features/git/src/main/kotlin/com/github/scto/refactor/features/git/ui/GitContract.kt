package com.github.scto.refactor.features.git.ui

// Contract zur Definition der UI-Zustände und Events
object GitContract {

    // Verschiedene Zustände der UI
    sealed interface GitUiState {
        object Loading : GitUiState

        data class Success(
            val repoUrl: String = "https://github.com/user/repo.git",
            val localPath: String,
            val branchName: String = "main",
            val statusMessage: String = "Bereit.",
            val email: String = "",
            val credentialsUsername: String =
                "", // Separates Feld für den Benutzernamen der Credentials
            val credentialsToken: String = "", // Separates Feld für das Passwort/Token
        ) : GitUiState

        data class Error(val message: String) : GitUiState
    }

    // Events, die von der UI ausgelöst werden können
    sealed interface GitEvent {
        data class OnRepoUrlChange(val newUrl: String) : GitEvent

        data class OnLocalPathChange(val newPath: String) : GitEvent

        data class OnBranchNameChange(val newName: String) : GitEvent

        // HINZUGEFÜGT: Fehlendes Event für E-Mail-Änderungen
        data class OnEmailChange(val newEmail: String) : GitEvent

        data class OnCredentialsUsernameChange(val username: String) : GitEvent

        data class OnCredentialsTokenChange(val token: String) : GitEvent

        object OnCloneClick : GitEvent

        object OnPullClick : GitEvent

        object SaveSettings : GitEvent
    }

    // Effekte für einmalige Aktionen (z.B. Toasts)
    sealed interface GitEffect {
        data class ShowToast(val message: String) : GitEffect
    }
}
