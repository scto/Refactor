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

enum class ThemeSetting {
    SYSTEM,
    LIGHT,
    DARK,
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
    val dynamicColor: Boolean = false, // Geändert zu Boolean für den Switch
	val apiKey: String = "", // Beispiel-Version
	val debug: Boolean = false, // Geändert zu Boolean für den Switch
    val appVersion: String = "1.0.0", // Beispiel-Version
)

/** Definiert die UI-Events, die von den Einstellungsbildschirmen ausgelöst werden können. */
sealed class SettingsUiEvent {
    /** Wird ausgelöst, wenn der Benutzer das Theme ändert. */
    data class OnThemeChanged(val theme: ThemeSetting) : SettingsUiEvent()

    /** Wird ausgelöst, wenn der Benutzer den Schalter für dynamische Farben betätigt. */
    data class OnDynamicColorChanged(val dynamicColor: Boolean) : SettingsUiEvent()

    /**
     * Wird ausgelöst, wenn sich der Api Key ändert (z.B. für die Anzeige). HINWEIS:
     * Syntaxfehler aus der Originaldatei wurde korrigiert.
     */
    data class OnApiKeyChanged(val apiKey: String) : SettingsUiEvent()
	
	/** Wird ausgelöst, wenn der Benutzer den Schalter für debugging betätigt. */
    data class OnDebugChanged(val debug: Boolean) : SettingsUiEvent()
	
    /**
     * Wird ausgelöst, wenn sich die App-Version ändert (z.B. für die Anzeige). HINWEIS:
     * Syntaxfehler aus der Originaldatei wurde korrigiert.
     */
    data class OnAppVersionChanged(val appVersion: String) : SettingsUiEvent()
}