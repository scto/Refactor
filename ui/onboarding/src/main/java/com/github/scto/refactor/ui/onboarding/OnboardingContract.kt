package com.github.scto.refactor.ui.onboarding

import androidx.annotation.DrawableRes
import com.github.scto.refactor.R // Passe dies an, um auf deine R-Klasse zuzugreifen

// Definiert die Daten für eine einzelne Onboarding-Seite
data class OnboardingPage(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)

// Liste der Onboarding-Seiten
val onboardingPages = listOf(
    OnboardingPage(
        title = "Willkommen bei Refactor",
        description = "Dein intelligenter Assistent zur Modernisierung von Android-Projekten.",
        image = R.drawable.ic_onboarding_1 // Ersetze dies mit deinen eigenen Grafiken
    ),
    OnboardingPage(
        title = "Prozessoren auswählen",
        description = "Wähle aus einer Vielzahl von Prozessoren, um deinen Code zu analysieren und zu verbessern.",
        image = R.drawable.ic_onboarding_2
    ),
    OnboardingPage(
        title = "Los geht's!",
        description = "Starte jetzt und bringe dein Projekt auf das nächste Level.",
        image = R.drawable.ic_onboarding_3
    )
)

// UI-Zustand für den Onboarding-Screen
data class OnboardingUiState(
    val currentPage: Int = 0,
    val isFinished: Boolean = false
)

// UI-Events, die vom Screen ausgelöst werden können
sealed class OnboardingUiEvent {
    data class PageChanged(val page: Int) : OnboardingUiEvent()
    object CompleteOnboarding : OnboardingUiEvent()
}