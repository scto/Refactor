package com.github.scto.refactor.ui.home

import android.net.Uri

enum class Processor(val description: String) {
    JAVATOKOTLIN("Java to Kotlin"),
    GRADLEGROOVYTOKTS("Groovy to KTS (Gradle)"),
    THEMETOMATERIAL3("XML Theme to Material3"),
    MANIFESTPROCESSOR("Manifest Modernizer"),
    SELFMODERNIZER("Project Self-Modernizer"),
    SVGTOAVD("SVG to Vector Drawable"),
    XMLTOCOMPOSE("XML Layout to Compose"),
    CODEANALYSIS("Code Analysis"),
    KOTLINSTYLECHECKER("Kotlin Style Checker")
}

data class RefactorUiState(
    val projectPath: Uri? = null,
    val oldPackageName: String = "",
    val newPackageName: String = "",
    val selectedProcessors: Map<Processor, Boolean> = Processor.values().associateWith { false },
    val selectedTabIndex: Int = 0,
    val showLog: Boolean = false,
    val showSuggestions: Boolean = true,
    val showResult: Boolean = true,
    val isRunning: Boolean = false
)

sealed class RefactorUiEvent {
    data class ProjectPathSelected(val uri: Uri) : RefactorUiEvent()
    data class OldPackageNameChanged(val name: String) : RefactorUiEvent()
    data class NewPackageNameChanged(val name: String) : RefactorUiEvent()
    data class ToggleProcessor(val processor: Processor) : RefactorUiEvent()
    data class TabSelected(val index: Int) : RefactorUiEvent()
    object ToggleShowLog : RefactorUiEvent()
    object ToggleShowSuggestions : RefactorUiEvent()
    object ToggleShowResult : RefactorUiEvent()
    object StartRefactoring : RefactorUiEvent()
}
