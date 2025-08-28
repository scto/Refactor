package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

object KotlinStyleProcessor : Processor {
    override val id = RefactoringOption.ANALYZE_KOTLIN_STYLE
    override val name: String = "Kotlin Stil-Analyse"
    override val description: String =
        "Analysiert Kotlin-Dateien und schlägt idiomatische Verbesserungen vor."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        val kotlinFiles =
            config.projectRoot.walk().filter { it.isFile && it.extension == "kt" }.toList()
        emit("${kotlinFiles.size} Kotlin-Dateien werden analysiert...")
        Timber.tag("KotlinStyleProcessor")
            .d("${kotlinFiles.size} Kotlin-Dateien werden analysiert...")

        kotlinFiles.forEach { file ->
            val fileContent = file.readText()
            if (fileContent.isNotBlank()) {
                emit("  -> Analysiere ${file.name}...")
                Timber.tag("KotlinStyleProcessor").d("Analysiere ${file.name}...")

                val result = GeminiProcessor.analyzeKotlinStyle(file.name, fileContent)

                result.fold(
                    onSuccess = { suggestion ->
                        if (suggestion.isNotBlank() && suggestion != fileContent) {
                            emit(
                                "     VORSCHLAG für ${file.name}:\n$suggestion\n--------------------"
                            )
                            Timber.tag("KotlinStyleProcessor")
                                .d("VORSCHLAG für ${file.name}:\n$suggestion\n--------------------")
                        }
                    },
                    onFailure = {
                        val errorMessage = "     FEHLER bei Analyse von ${file.name}."
                        emit(errorMessage)
                        Timber.tag("KotlinStyleProcessor").d(errorMessage)
                    },
                )
            }
        }
    }
}
