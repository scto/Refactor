package com.github.scto.refactor.core.gemini.model

data class RefactoringState(
    val progress: Float = 0f,
    val currentStep: String = "Bereit",
    val logs: List<String> = emptyList(),
    val isRunning: Boolean = false,
    val styleSuggestions: List<StyleSuggestion> = emptyList(),
)
