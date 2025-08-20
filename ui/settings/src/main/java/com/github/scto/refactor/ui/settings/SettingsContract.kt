package com.github.scto.refactor.ui.settings

enum class ThemeSetting {
    SYSTEM, LIGHT, DARK
}

data class SettingsUiState(
    val theme: ThemeSetting = ThemeSetting.SYSTEM,
    val appVersion: String = "1.0.0" // Beispiel-Version
)

sealed class SettingsUiEvent {
    data class OnThemeChanged(val theme: ThemeSetting) : SettingsUiEvent()
}