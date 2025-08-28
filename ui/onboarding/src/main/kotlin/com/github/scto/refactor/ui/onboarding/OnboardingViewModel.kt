package com.github.scto.refactor.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.scto.refactor.data.local.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class OnboardingViewModel
@Inject
constructor(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun handleEvent(event: OnboardingUiEvent) {
        viewModelScope.launch {
            when (event) {
                is OnboardingUiEvent.PageChanged -> {
                    _uiState.update { it.copy(currentPage = event.page) }
                }
                OnboardingUiEvent.CompleteOnboarding -> {
                    userPreferencesRepository.saveOnboardingCompleted(true)
                    _uiState.update { it.copy(isFinished = true) }
                }
            }
        }
    }
}
