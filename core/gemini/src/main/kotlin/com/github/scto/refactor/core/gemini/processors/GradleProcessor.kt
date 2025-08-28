package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

object GradleProcessor : Processor {
    override val id = RefactoringOption.REFACTOR_GRADLE
    override val name: String = "Gradle zu KTS Konverter"
    override val description: String = "Konvertiert Groovy-basierte Gradle-Skripte in Kotlin DSL."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        val gradleFiles =
            config.projectRoot
                .walk()
                .filter { it.isFile && (it.name == "build.gradle" || it.name == "settings.gradle") }
                .toList()
        if (gradleFiles.isEmpty()) {
            emit("Keine .gradle-Dateien gefunden. Überspringe.")
            Timber.tag("GradleProcessor").d("Keine .gradle-Dateien gefunden. Überspringe.")
            return@flow
        }

        gradleFiles.forEach { file ->
            emit("  -> Refaktorisiere ${file.name}...")
            Timber.tag("GradleProcessor").d("Refaktorisiere ${file.name}...")

            val result = GeminiProcessor.refactorGradleToKts(file.readText())

            result.fold(
                onSuccess = { kotlinCode ->
                    val ktsFile = File(file.parent, "${file.name}.kts")
                    ktsFile.writeText(kotlinCode)
                    file.delete()
                    emit("     Erfolgreich nach ${ktsFile.name} konvertiert.")
                    Timber.tag("GradleProcessor").d("Erfolgreich nach ${ktsFile.name} konvertiert.")
                },
                onFailure = { error ->
                    val errorMessage = "     FEHLER bei ${file.name}: ${error.localizedMessage}"
                    emit(errorMessage)
                    Timber.tag("GradleProcessor").e(errorMessage)
                },
            )
        }
    }
}
