package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

object ManifestProcessor : Processor {
    override val id = RefactoringOption.REFACTOR_MANIFEST
    override val name: String = "AndroidManifest Modernisierer"
    override val description: String = "Korrigiert das AndroidManifest für moderne Android-Versionen."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        val manifestFile = config.projectRoot.walk().find { it.name == "AndroidManifest.xml" }
        if (manifestFile == null) {
            emit("WARNUNG: Konnte AndroidManifest.xml nicht finden. Überspringe.")
            Timber.tag("ManifestProcessor").d("Konnte AndroidManifest.xml nicht finden. Überspringe.")
            return@flow
        }

        emit("Analysiere ${manifestFile.path}...")
        Timber.tag("ManifestProcessor").d("Analysiere ${manifestFile.path}...")
        val originalXml = manifestFile.readText()
        val result = GeminiProcessor.analyzeAndRefactorManifest(originalXml)

        // KORRIGIERT: Result-Objekt wird jetzt korrekt mit fold behandelt
        result.fold(
            onSuccess = { refactoredXml ->
                if (originalXml != refactoredXml) {
                    manifestFile.writeText(refactoredXml)
                    emit("${manifestFile.name} erfolgreich aktualisiert.")
                    Timber.tag("ManifestProcessor").d("${manifestFile.name} erfolgreich aktualisiert.")
                } else {
                    emit("${manifestFile.name} ist bereits auf dem neuesten Stand.")
                    Timber.tag("ManifestProcessor").d("${manifestFile.name} ist bereits auf dem neuesten Stand.")
                }
            },
            onFailure = { error ->
                val errorMessage = "FEHLER bei der Analyse von ${manifestFile.name}: ${error.localizedMessage}"
                emit(errorMessage)
                Timber.tag("ManifestProcessor").e(errorMessage)
            }
        )
    }
}