package com.github.scto.refactor.core.gemini.processors

import com.github.scto.refactor.core.gemini.arch.Processor
import com.github.scto.refactor.core.gemini.config.RefactoringConfig
import com.github.scto.refactor.core.gemini.ui.RefactoringOption

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

import timber.log.Timber

import java.io.File

object JavaToKotlinProcessor : Processor {
    override val id = RefactoringOption.REFACTOR_JAVA_TO_KOTLIN
    override val name: String = "Java zu Kotlin Konverter"
    override val description: String = "Konvertiert alle .java-Dateien im Projekt in modernes Kotlin."

    override fun process(config: RefactoringConfig): Flow<String> = flow {
        val javaFiles = config.projectRoot.walk().filter { it.extension == "java" }.toList()
        if (javaFiles.isEmpty()) {
            emit("Keine Java-Dateien gefunden. Überspringe.")
            Timber.tag("JavaToKotlinProcessor").d("Keine Java-Dateien gefunden. Überspringe.")
            return@flow
        }

        emit("${javaFiles.size} Java-Dateien gefunden. Starte Konvertierung...")
        Timber.tag("JavaToKotlinProcessor").d("${javaFiles.size} Java-Dateien gefunden. Starte Konvertierung...")
        
        javaFiles.forEach { javaFile ->
            withContext(Dispatchers.IO) {
                emit("  -> Konvertiere ${javaFile.name}...")
                Timber.tag("JavaToKotlinProcessor").d("Konvertiere ${javaFile.name}...")

                val result = GeminiProcessor.convertJavaToKotlin(javaFile.readText())

                result.fold(
                    onSuccess = { kotlinCode ->
                        val kotlinFile = File(javaFile.parent, "${javaFile.nameWithoutExtension}.kt")
                        kotlinFile.writeText(kotlinCode)
                        javaFile.delete()
                        emit("     Erfolgreich nach ${kotlinFile.name} konvertiert.")
                        Timber.tag("JavaToKotlinProcessor").d("Erfolgreich nach ${kotlinFile.name} konvertiert.")
                    },
                    onFailure = { error ->
                        val errorMessage = "     FEHLER bei ${javaFile.name}: ${error.localizedMessage}"
                        emit(errorMessage)
                        Timber.tag("JavaToKotlinProcessor").e(errorMessage)
                    }
                )
            }
        }
    }
}