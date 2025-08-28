package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.io.File

object SelfModernizationProcessor : Processor {
    override val id = RefactoringOption.SELF_MODERNIZE
    override val name = "Selbst-Modernisierung durchführen"
    override val description = "Analysiert den eigenen Code und schlägt Verbesserungen vor."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        emit("Starte Selbst-Analyse des Modernizer-Projekts...")
        Timber.tag("SelfModernizationProcessor").d("Starte Selbst-Analyse des Modernizer-Projekts...")
        val projectFiles = config.projectRoot.walk()
            .filter { it.isFile && it.extension in listOf("kt", "kts", "xml") }
            .take(15)
            .map { it.name to it.readText() }
            .toList()

        if (projectFiles.isEmpty()) {
            emit("Keine relevanten Projektdateien für die Selbst-Analyse gefunden.")
            Timber.tag("SelfModernizationProcessor").d("Keine relevanten Projektdateien für die Selbst-Analyse gefunden.")
            return@flow
        }

        emit("${projectFiles.size} Dateien werden für die Meta-Analyse vorbereitet...")
        Timber.tag("SelfModernizationProcessor").d("${projectFiles.size} Dateien werden für die Meta-Analyse vorbereitet...")
        
        // KORRIGIERT: Falschen Funktionsaufruf korrigiert
        val analysisResult = GeminiProcessor.selfModernizer(projectFiles)

        analysisResult.fold(
            onSuccess = { result ->
                emit("--- Meta-Analyse Ergebnis ---")
                Timber.tag("SelfModernizationProcessor").d("--- Meta-Analyse Ergebnis ---")
                emit(result)
                Timber.tag("SelfModernizationProcessor").d(result)
                emit("-----------------------------")
                Timber.tag("SelfModernizationProcessor").d("-----------------------------")
            },
            onFailure = { error ->
                val errorMessage = "FEHLER bei der Selbst-Analyse: ${error.localizedMessage}"
                emit(errorMessage)
                Timber.tag("SelfModernizationProcessor").e(errorMessage)
            }
        )
    }
}