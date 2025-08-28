package com.github.scto.refactor.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.scto.refactor.data.local.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
                userPreferencesRepository
                    .useDynamicColor, // Annahme: Dieser Flow existiert im Repository
            ) { themeName, useDynamicColor ->
                SettingsUiState(
                    theme =
                        try {
                            ThemeSetting.valueOf(themeName)
                        } catch (e: IllegalArgumentException) {
                            ThemeSetting.SYSTEM // Fallback, falls der Wert ungültig ist
                        },
                    useDynamicColor = useDynamicColor,
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
                    userPreferencesRepository.saveUseDynamicColor(event.useDynamicColor)
                }
                is SettingsUiEvent.OnAppVersionChanged -> {
                    // Hier könnte zukünftige Logik implementiert werden
                }
            }
        }
    }
}
