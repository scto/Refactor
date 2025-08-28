package com.github.scto.refactor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.scto.refactor.data.local.UserPreferencesRepository
import com.github.scto.refactor.ui.settings.ThemeSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel
@Inject
constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val onboardingCompleted =
        userPreferencesRepository.onboardingCompleted.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            false,
        )

    // NEU: Theme-Flow hinzugefügt, der von der UI benötigt wird.
    val theme =
        userPreferencesRepository.theme
            .map { ThemeSetting.valueOf(it) }
            .stateIn(viewModelScope, SharingStarted.Lazily, ThemeSetting.SYSTEM)

    fun setOnboardingCompleted(completed: Boolean) {
        viewModelScope.launch { userPreferencesRepository.saveOnboardingCompleted(completed) }
    }
}
