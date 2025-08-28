package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

object ThemeProcessor : Processor {
    override val id = RefactoringOption.REFACTOR_THEMES
    override val name: String = "Theme zu Material 3 Konverter"
    override val description: String =
        "Konvertiert alte XML-Themes in ein modernes Material 3 Theme."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        val resDir = config.projectRoot.walk().find { it.isDirectory && it.name == "res" }
        if (resDir == null) {
            emit("WARNUNG: Konnte kein 'res'-Verzeichnis finden. Überspringe.")
            return@flow
        }

        val valuesDir = File(resDir, "values")
        val nightValuesDir = File(resDir, "values-night")
        val oldThemesFile = File(valuesDir, "themes.xml")
        val oldColorsFile = File(valuesDir, "colors.xml")

        if (!oldThemesFile.exists() || !oldColorsFile.exists()) {
            emit("WARNUNG: Konnte themes.xml oder colors.xml nicht finden.")
            return@flow
        }

        emit("Konvertiere Themes nach Material 3...")
        val result =
            withContext(Dispatchers.IO) {
                GeminiProcessor.convertThemeToMaterial3(
                    oldThemesFile.readText(),
                    oldColorsFile.readText(),
                )
            }

        // KORRIGIERT: Result-Objekt wird jetzt korrekt mit fold behandelt
        result.fold(
            onSuccess = { newThemesXml ->
                oldThemesFile.writeText(newThemesXml)
                emit("  -> themes.xml erfolgreich zu Material 3 konvertiert.")
                val nightThemeFile = File(nightValuesDir, "themes.xml")
                if (nightThemeFile.exists()) {
                    nightThemeFile.delete()
                    emit("  -> Veraltete values-night/themes.xml gelöscht.")
                }
            },
            onFailure = { error ->
                emit("  -> FEHLER bei der Theme-Konvertierung: ${error.localizedMessage}")
            },
        )
    }
}
