package com.github.scto.refactor.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class RefactorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RefactorUiState())
    val uiState: StateFlow<RefactorUiState> = _uiState.asStateFlow()

    fun handleEvent(event: RefactorUiEvent) {
        viewModelScope.launch {
            when (event) {
                is RefactorUiEvent.ProjectPathSelected -> _uiState.update { it.copy(projectPath = event.uri) }
                is RefactorUiEvent.OldPackageNameChanged -> _uiState.update { it.copy(oldPackageName = event.name) }
                is RefactorUiEvent.NewPackageNameChanged -> _uiState.update { it.copy(newPackageName = event.name) }
                is RefactorUiEvent.ToggleProcessor -> {
                    val updated = _uiState.value.selectedProcessors.toMutableMap()
                    updated[event.processor] = !updated.getValue(event.processor)
                    _uiState.update { it.copy(selectedProcessors = updated) }
                }
                is RefactorUiEvent.TabSelected -> _uiState.update { it.copy(selectedTabIndex = event.index) }
                RefactorUiEvent.ToggleShowLog -> _uiState.update { it.copy(showLog = !it.showLog) }
                RefactorUiEvent.ToggleShowSuggestions -> _uiState.update { it.copy(showSuggestions = !it.showSuggestions) }
                RefactorUiEvent.ToggleShowResult -> _uiState.update { it.copy(showResult = !it.showResult) }
                RefactorUiEvent.StartRefactoring -> Timber.i("Refactoring process started!")
            }
        }
    }
}
