package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

object SvgToAvdProcessor : Processor {
    override val id = RefactoringOption.CONVERT_SVG_TO_AVD
    override val name: String = "SVG zu Android Vector Drawable Konverter"
    override val description: String = "Konvertiert .svg-Grafiken in das Android-spezifische XML-Format."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        emit("Starte Suche nach .svg-Dateien...")
        val svgFiles = config.projectRoot.walk()
            .filter { it.isFile && it.extension.equals("svg", ignoreCase = true) }
            .toList()

        if (svgFiles.isEmpty()) {
            emit("Keine .svg-Dateien im Projekt gefunden. Überspringe.")
            return@flow
        }

        emit("${svgFiles.size} SVG-Dateien gefunden. Starte Konvertierung...")
        val drawableDir = config.projectRoot.walk().find { it.isDirectory && it.name == "drawable" }
        if (drawableDir == null) {
            emit("FEHLER: Konnte kein 'drawable'-Verzeichnis im Projekt finden, um die Ergebnisse zu speichern.")
            return@flow
        }
        
        emit("Ergebnisse werden in '${drawableDir.relativeTo(config.projectRoot)}' gespeichert.")

        svgFiles.forEach { svgFile ->
            withContext(Dispatchers.IO) {
                emit("  -> Verarbeite ${svgFile.name}...")
                val svgContent = svgFile.readText()
                val result = GeminiProcessor.convertSvgToAvd(svgContent)

                // KORRIGIERT: Result-Objekt wird jetzt korrekt mit fold behandelt
                result.fold(
                    onSuccess = { avdContent ->
                        if (avdContent.contains("<vector")) {
                            val newFileName = svgFile.nameWithoutExtension.lowercase().replace("-", "_") + ".xml"
                            val newFile = File(drawableDir, newFileName)
                            newFile.writeText(avdContent)
                            emit("     Erfolgreich nach ${newFile.name} konvertiert.")
                        } else {
                            emit("     FEHLER bei der Konvertierung von ${svgFile.name}: Die Antwort war kein gültiges Vector Drawable.")
                        }
                    },
                    onFailure = { error ->
                        emit("     KRITISCHER FEHLER bei der Verarbeitung von ${svgFile.name}: ${error.message}")
                    }
                )
            }
        }
    }
}