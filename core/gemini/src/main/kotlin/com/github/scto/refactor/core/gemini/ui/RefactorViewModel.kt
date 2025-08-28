package com.github.scto.refactor.core.gemini.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.core.RefactoringOrchestrator
import com.github.scto.refactor.core.gemini.model.StyleSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RefactorViewModel @Inject constructor(
   private val orchestrator: RefactoringOrchestrator
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
   val uiState: StateFlow<UiState> = _uiState.asStateFlow()

   private var refactoringJob: Job? = null

   fun onEvent(event: UiEvent) {
       when (event) {
           is UiEvent.ProjectPathChanged -> _uiState.update { it.copy(projectPath = event.newPath) }
           is UiEvent.TargetPackageNameChanged -> _uiState.update { it.copy(targetPackageName = event.newName) }
           is UiEvent.RefactoringOptionToggled -> toggleRefactoringOption(event.option, event.isEnabled)
           is UiEvent.StartRefactoringClicked -> startRefactoring()
           is UiEvent.ApplySuggestion -> applySuggestion(event.suggestion)
       }
   }

   private fun toggleRefactoringOption(option: RefactoringOption, isEnabled: Boolean) {
       _uiState.update { currentState ->
           val newOptions = if (isEnabled) {
               currentState.options + option
           } else {
               currentState.options - option
           }
           currentState.copy(options = newOptions)
       }
   }

   private fun startRefactoring() {
       if (refactoringJob?.isActive == true) return

       val currentState = _uiState.value

       refactoringJob = viewModelScope.launch {
           try {
               _uiState.update {
                   it.copy(
                       isRunning = true,
                       logs = emptyList(),
                       styleSuggestions = emptyList(),
                       analysisResults = emptyList(),
                       currentStep = "Starte Analyse..."
                   )
               }

               val config = currentState.toRefactoringConfig()

               orchestrator.start(config)
                   .onEach { logMessage ->
                       _uiState.update { it.copy(logs = it.logs + logMessage) }
                   }
                   .collect()

               _uiState.update { it.copy(currentStep = "Refactoring erfolgreich abgeschlossen!") }

           } catch (e: Exception) {
               _uiState.update {
                   it.copy(currentStep = "Fehler: ${e.localizedMessage ?: "Unbekannter Fehler"}")
               }
           } finally {
               _uiState.update { it.copy(isRunning = false) }
           }
       }
   }

   private fun applySuggestion(suggestion: StyleSuggestion) {
       viewModelScope.launch {
           try {
               File(suggestion.filePath).writeText(suggestion.suggestion)
               _uiState.update { currentState ->
                   currentState.copy(
                       styleSuggestions = currentState.styleSuggestions - suggestion
                   )
               }
           } catch (e: Exception) {
               _uiState.update {
                   it.copy(logs = it.logs + "Fehler beim Anwenden des Vorschlags: ${e.message}")
               }
           }
       }
   }
}

private fun UiState.toRefactoringConfig(): RefactoringConfig {
   val detectedOldPackageName = "com.example.old"

   return RefactoringConfig(
       projectPath = this.projectPath,
       oldPackageName = detectedOldPackageName,
       newPackageName = this.targetPackageName,
       analyzeDependencies = RefactoringOption.ANALYZE_DEPENDENCIES in this.options,
       refactorGradle = RefactoringOption.REFACTOR_GRADLE in this.options,
       extractStrings = RefactoringOption.EXTRACT_STRINGS in this.options,
       refactorJavaToKotlin = RefactoringOption.REFACTOR_JAVA_TO_KOTLIN in this.options,
       analyzeKotlinStyle = RefactoringOption.ANALYZE_KOTLIN_STYLE in this.options,
       refactorManifest = RefactoringOption.REFACTOR_MANIFEST in this.options,
       renamePackage = RefactoringOption.RENAME_PACKAGE in this.options,
       selfModernize = RefactoringOption.SELF_MODERNIZE in this.options,
       convertSvgToAvd = RefactoringOption.CONVERT_SVG_TO_AVD in this.options,
       refactorThemes = RefactoringOption.REFACTOR_THEMES in this.options,
       // KORRIGIERT: An die neuen Enum-Namen angepasst
       xmlToCompose = RefactoringOption.XML_TO_COMPOSE in this.options,
       codeAnalysis = RefactoringOption.CODE_ANALYSIS in this.options
   )
}