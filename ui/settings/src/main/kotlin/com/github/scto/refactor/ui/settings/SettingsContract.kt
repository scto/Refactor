package com.github.scto.refactor.ui.settings

enum class ThemeSetting {
    SYSTEM, LIGHT, DARK
}

/**
 * Repräsentiert den UI-Zustand für die Einstellungsbildschirme.
 *
 * @param theme Die aktuell ausgewählte Theme-Einstellung.
 * @param useDynamicColor Gibt an, ob dynamische Farben aktiviert sind.
 * @param appVersion Die aktuelle Version der App.
 */
data class SettingsUiState(
    val theme: ThemeSetting = ThemeSetting.SYSTEM,
    val useDynamicColor: Boolean = false, // Geändert zu Boolean für den Switch
    val appVersion: String = "1.0.0" // Beispiel-Version
)

/**
 * Definiert die UI-Events, die von den Einstellungsbildschirmen ausgelöst werden können.
 */
sealed class SettingsUiEvent {
    /**
     * Wird ausgelöst, wenn der Benutzer das Theme ändert.
     */
    data class OnThemeChanged(val theme: ThemeSetting) : SettingsUiEvent()

    /**
     * Wird ausgelöst, wenn der Benutzer den Schalter für dynamische Farben betätigt.
     */
    data class OnDynamicColorChanged(val useDynamicColor: Boolean) : SettingsUiEvent()

    /**
     * Wird ausgelöst, wenn sich die App-Version ändert (z.B. für die Anzeige).
     * HINWEIS: Syntaxfehler aus der Originaldatei wurde korrigiert.
     */
    data class OnAppVersionChanged(val appVersion: String) : SettingsUiEvent()
}
