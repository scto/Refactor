package com.github.scto.refactor.core.gemini.ui

import com.github.scto.refactor.core.gemini.model.AnalysisResult
import com.github.scto.refactor.core.gemini.model.StyleSuggestion

data class UiState(
   val projectPath: String = "",
   val targetPackageName: String = "",
   val options: Set<RefactoringOption> = setOf(RefactoringOption.RENAME_PACKAGE),
   val logs: List<String> = emptyList(),
   val styleSuggestions: List<StyleSuggestion> = emptyList(),
   val analysisResults: List<AnalysisResult> = emptyList(),
   val isRunning: Boolean = false,
   val currentStep: String = "Bereit"
)

sealed interface UiEvent {
   data class ProjectPathChanged(val newPath: String) : UiEvent
   data class TargetPackageNameChanged(val newName: String) : UiEvent
   data class RefactoringOptionToggled(val option: RefactoringOption, val isEnabled: Boolean) : UiEvent
   object StartRefactoringClicked : UiEvent
   data class ApplySuggestion(val suggestion: StyleSuggestion) : UiEvent
}

enum class RefactoringOption(val displayText: String) {
   ANALYZE_DEPENDENCIES("Veraltete Abhängigkeiten finden"),
   REFACTOR_GRADLE("Gradle-Skripte zu .kts konvertieren"),
   EXTRACT_STRINGS("Hartcodierte Strings extrahieren"),
   REFACTOR_JAVA_TO_KOTLIN("Java-Dateien nach Kotlin konvertieren"),
   ANALYZE_KOTLIN_STYLE("Kotlin-Code-Stil analysieren"),
   REFACTOR_MANIFEST("AndroidManifest.xml modernisieren"),
   RENAME_PACKAGE("Paketname umbenennen"),
   SELF_MODERNIZE("Projekt-Selbstanalyse durchführen"),
   CONVERT_SVG_TO_AVD("SVGs zu Vector Drawables konvertieren"),
   REFACTOR_THEMES("XML-Themes zu Material 3 konvertieren"),
   // VERBESSERT: Enum-Namen an Kotlin-Konventionen angepasst für bessere Lesbarkeit.
   XML_TO_COMPOSE("XML-Layouts in Jetpack Compose umwandeln"),
   CODE_ANALYSIS("Statische Code-Analyse durchführen")
}
