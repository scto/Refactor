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

// KORRIGIERT: Import auf die R-Klasse des eigenen Moduls geändert
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

import kotlinx.coroutines.launch

// Definiert die Daten für eine einzelne Onboarding-Seite
data class OnboardingPage(@StringRes val title: String, @StringRes val description: String, @DrawableRes val image: Int)

// WICHTIGER HINWEIS:
// Sie müssen noch die drei Bilder (ic_onboarding_1, ic_onboarding_2, ic_onboarding_3)
// im `res/drawable`-Verzeichnis dieses :ui:onboarding-Moduls erstellen,
// und im res/values eine strings.xml Datei mit den strings
// damit der Build erfolgreich ist.
val onboardingPages =
    listOf(
        OnboardingPage(
			title = R.string.onboarding_main_title,
			//title = Text(stringResource(id = R.string.onboarding_main_title)),
            //title = "Willkommen bei Refactor",
			description = R.string.onboarding_main_description,
			//description = Text(stringResource(id = R.string.onboarding_main_description)),
            //description = "Dein intelligenter Assistent zur Modernisierung von Android-Projekten.",
            image = R.drawable.ic_onboarding_1,
        ),
        OnboardingPage(
			title = R.string.onboarding_processors_title,
			//title = Text(stringResource(id = R.string.onboarding_processors_title)),
            //title = "Prozessoren auswählen",
			description = R.string.onboarding_processors_description,
			//description = Text(stringResource(id = R.string.onboarding_processors_description)),
            //description = "Wähle aus einer Vielzahl von Prozessoren, um deinen Code zu analysieren und zu verbessern.",
            image = R.drawable.ic_onboarding_2,
        ),
        OnboardingPage(
			title = R.string.onboarding_run_title,
			//title = Text(stringResource(id = R.string.onboarding_processors_title)),
            //title = "Los geht's!",
			description = R.string.onboarding_run_description,
			//description = Text(stringResource(id = R.string.onboarding_processors_description)),
            //description = "Starte jetzt und bringe dein Projekt auf das nächste Level.",
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
