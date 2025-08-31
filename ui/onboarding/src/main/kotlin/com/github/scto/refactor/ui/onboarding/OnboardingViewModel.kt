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
package com.github.scto.refactor.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

import com.github.scto.refactor.data.local.UserPreferencesRepository

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