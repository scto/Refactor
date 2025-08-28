package com.github.scto.refactor.ui.onboarding

// KORRIGIERT: Import auf die R-Klasse des eigenen Moduls geändert
import androidx.annotation.DrawableRes

// Definiert die Daten für eine einzelne Onboarding-Seite
data class OnboardingPage(val title: String, val description: String, @DrawableRes val image: Int)

// WICHTIGER HINWEIS: Sie müssen noch die drei Bilder (ic_onboarding_1, ic_onboarding_2,
// ic_onboarding_3)
// im `res/drawable`-Verzeichnis dieses :ui:onboarding-Moduls erstellen, damit der Build erfolgreich
// ist.
val onboardingPages =
    listOf(
        OnboardingPage(
            title = "Willkommen bei Refactor",
            description = "Dein intelligenter Assistent zur Modernisierung von Android-Projekten.",
            image = R.drawable.ic_onboarding_1,
        ),
        OnboardingPage(
            title = "Prozessoren auswählen",
            description =
                "Wähle aus einer Vielzahl von Prozessoren, um deinen Code zu analysieren und zu verbessern.",
            image = R.drawable.ic_onboarding_2,
        ),
        OnboardingPage(
            title = "Los geht's!",
            description = "Starte jetzt und bringe dein Projekt auf das nächste Level.",
            image = R.drawable.ic_onboarding_3,
        ),
    )

// UI-Zustand für den Onboarding-Screen
data class OnboardingUiState(val currentPage: Int = 0, val isFinished: Boolean = false)

// UI-Events, die vom Screen ausgelöst werden können
sealed class OnboardingUiEvent {
    data class PageChanged(val page: Int) : OnboardingUiEvent()

    object CompleteOnboarding : OnboardingUiEvent()
}
