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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

import com.github.scto.refactor.data.local.UserPreferencesRepository

@HiltViewModel
class SettingsViewModel
@Inject
constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    /**
     * Ein Flow, der den aktuellen UI-Zustand der Einstellungen repräsentiert. Er kombiniert die
     * Einstellungen für das Theme und die dynamischen Farben aus dem Repository.
     */
    val uiState: StateFlow<SettingsUiState> =
        combine(
                userPreferencesRepository.theme,
                userPreferencesRepository.dynamicColor, // Annahme: Dieser Flow existiert im Repository
				userPreferencesRepository.apiKey,
				userPreferencesRepository.debug,
				userPreferencesRepository.appVersion,
				
            ) { themeName, dynamicColor, apiKey, debug, appVersion, ->
                SettingsUiState(
                    theme =
                        try {
                            ThemeSetting.valueOf(themeName)
                        } catch (e: IllegalArgumentException) {
                            ThemeSetting.SYSTEM // Fallback, falls der Wert ungültig ist
                        },
                    dynamicColor = dynamicColor,
					apiKey = apiKey,
					debug = debug,
					appVersion = appVersion,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState(),
            )

    /** Verarbeitet UI-Events, die von den Einstellungs-Composables gesendet werden. */
    fun handleEvent(event: SettingsUiEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsUiEvent.OnThemeChanged -> {
                    userPreferencesRepository.saveTheme(event.theme.name)
                }
                is SettingsUiEvent.OnDynamicColorChanged -> {
                    // Speichert den neuen Boolean-Wert für die dynamischen Farben
                    userPreferencesRepository.saveDynamicColor(event.dynamicColor)
                }
				is SettingsUiEvent.OnApiKeyChanged -> {
                    // Speichert den neuen Boolean-Wert für die dynamischen Farben
                    userPreferencesRepository.saveApiKey(event.apiKey)
                }
				is SettingsUiEvent.OnDebugChanged -> {
                    // Speichert den neuen Boolean-Wert für die dynamischen Farben
                    userPreferencesRepository.saveDebug(event.debug)
                }
                is SettingsUiEvent.OnAppVersionChanged -> {
                    // Hier könnte zukünftige Logik implementiert werden
					userPreferencesRepository.saveAppVersion(event.appVersion)
                }
            }
        }
    }
}